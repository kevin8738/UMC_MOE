package erd.exmaple.erd.example.domain.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    @Qualifier("customUserDetailService")
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        logger.info("Authorization header: {}", authorizationHeader);

        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            logger.info("Received JWT: {}", jwt);

            try {
                username = jwtUtil.extractUsername(jwt);
                logger.info("Extracted username from JWT: {}", username);
            } catch (IllegalArgumentException e) {
                logger.error("Unable to get JWT Token", e);
            } catch (ExpiredJwtException e) {
                logger.error("JWT Token has expired", e);
            } catch (SignatureException e) {
                logger.error("JWT signature does not match locally computed signature: {}", e);
            }
        } else {
            logger.info("Authorization header is missing or does not start with Bearer");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            logger.info("Loaded user details: {}", userDetails);

            if (jwtUtil.validateToken(jwt, userDetails)) {
                logger.info("JWT token is valid");
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                logger.info("Authentication is set for user: {}", username);
            } else {
                logger.info("JWT token is not valid");
            }
        } else {
            logger.info("Username is null or authentication is already set");
        }

        chain.doFilter(request, response);
    }
}
