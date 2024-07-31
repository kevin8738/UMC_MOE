package erd.exmaple.erd.example.domain.jwt;


import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    @Qualifier("customUserDetailService")
    private UserDetailsService userDetailsService;

    private static final Logger log = LoggerFactory.getLogger(JwtRequestFilter.class);

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private JwtBlacklistService jwtBlacklistService;


/*     *이 메서드는 각 요청마다 실행되며 JWT 토큰을 검사하여 유효한 경우
      Spring Security의 컨텍스트에 인증 정보를 설정합니다.

      @param request  HttpServletRequest 객체
    @param response HttpServletResponse 객체
     @param chain    FilterChain 객체
    @throws ServletException
     @throws IOException*/


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        // 요청 헤더에서 "Authorization" 헤더를 가져옵
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;
        // "Authorization" 헤더가 존재하고 "Bearer "로 시작하는지 확인
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // "Bearer " 이후의 JWT 토큰을 추출
            jwt = authorizationHeader.substring(7);
            log.info("Received JWT: {}", jwt);
            // JWT 토큰에서 사용자 이름을 추출
            if (jwtBlacklistService.isBlacklisted(jwt)) {
                logger.info("JWT token is blacklisted");
                chain.doFilter(request, response);
                return;
            }
            try {
                username = jwtUtil.extractUsername(jwt);
                logger.info("Extracted username from JWT: " + username);
            } catch (SignatureException e) {
                logger.error("JWT signature does not match locally computed signature: {}", e);
            }
        }
        // 사용자 이름이 존재하고 SecurityContext에 인증 정보가 없는 경우
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // UserDetailsService를 사용하여 사용자 정보를 로드
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            // JWT 토큰이 유효한지 검증
            if (jwtUtil.validateToken(jwt, userDetails)) {
                logger.info("JWT token is valid");
                // 유효한 토큰인 경우, Spring Security의 컨텍스트에 인증 정보를 설정
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                logger.info("Authentication is set for user: " + username);
            } else {
                logger.info("JWT token is not valid");
            }
        } else {
            logger.info("Username is null or authentication is already set");
        }

        logger.info("Authentication: " + SecurityContextHolder.getContext().getAuthentication());
        // 다음 필터를 호출.
        chain.doFilter(request, response);
    }
}