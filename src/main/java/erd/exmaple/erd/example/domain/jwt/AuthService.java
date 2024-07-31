
//import erd.exmaple.erd.example.domain.jwt.JwtUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.stereotype.Service;
//
//@Service
//public class AuthService {
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private UserDetailsService userDetailsService;
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    public String createJwtToken(String phoneNumber, String password) throws Exception {
//        try {
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(phoneNumber, password));
//        } catch (Exception e) {
//            throw new Exception("Invalid phone number or password", e);
//        }
//
//        UserDetails userDetails = userDetailsService.loadUserByUsername(phoneNumber);
//        return jwtUtil.generateToken(userDetails);
//    }
//
//    public boolean validateJwtToken(String token, UserDetails userDetails) {
//        return jwtUtil.validateToken(token, userDetails);
//    }
//}