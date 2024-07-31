package erd.exmaple.erd.example.domain.config;

import erd.exmaple.erd.example.domain.service.UserService.CustomOAuth2UserService;
import erd.exmaple.erd.example.domain.jwt.JwtRequestFilter;
import erd.exmaple.erd.example.domain.jwt.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {


    @Autowired
    @Qualifier("customUserDetailService")
    private UserDetailsService customUserDetailsService; // 사용자 세부 정보를 로드하기 위한 서비스

    @Autowired
    private JwtRequestFilter jwtRequestFilter; // JWT 요청 필터

    private final CustomUserDetailService userDetailsService;
    @Autowired
    private CustomOAuth2UserService customOAuth2UserService; // CustomOAuth2UserService 주입

    public SecurityConfig(CustomUserDetailService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                //.requestMatchers("/", "/user/login", "/user/kakao", "/user/naver", "/user/google", "/user/kakao/callback", "/user/naver/callback", "/user/google/callback", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                                .requestMatchers("/", "/user/**","/user/join","/user/login").permitAll() // "/" 및 "/api/users/**" 경로는 모든 사용자에게 허용합
                                .requestMatchers( "/swagger-ui.html", "/swagger-ui/**","/v3/api-docs/**").permitAll()//스웨거 설정
                                .anyRequest().permitAll()//임시허용 .authenticated()
                )
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/user/login")
                                .loginProcessingUrl("/user/perform_login")
                                .defaultSuccessUrl("/Moe/main", true)
                                .failureUrl("/user/login?error=true")
                )
                .logout(logout ->
                        logout
                                .logoutUrl("/user/perform_logout")
                                .deleteCookies("JSESSIONID")
                                .logoutSuccessUrl("/user/login")
                )
                .oauth2Login(oauth2Login ->
                        oauth2Login
                                .loginPage("/login.html")
                                .authorizationEndpoint(authorizationEndpoint ->
                                        authorizationEndpoint
                                                .baseUri("/oauth2/authorization")
                                )
                                .redirectionEndpoint(redirectionEndpoint ->
                                        redirectionEndpoint
                                                .baseUri("/login/oauth2/code/*")
                                )
                                .userInfoEndpoint(userInfoEndpoint ->
                                        userInfoEndpoint
                                                .userService(customOAuth2UserService)
                                )
                )
//                .oauth2Login(oauth2Login ->
//                        oauth2Login
//                                .loginPage("/login.html")
//                                .defaultSuccessUrl("/Moe/main", true)
//                                .failureUrl("/user/login?error=true")
//                                .userInfoEndpoint(userInfoEndpoint ->
//                                        userInfoEndpoint
//                                                .userService(customOAuth2UserService)
//                                )
//                )
                .cors(cors ->cors.disable())  // CORS 비활성화
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 관리 설정을 STATELESS(무상태)로 지정합니다.
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class); // JWT 필터 등록

//                .csrf(csrf -> csrf
//                        .ignoringRequestMatchers("/user/kakao/callback", "/user/naver/callback", "/user/google/callback") // 특정 경로에 대해 CSRF 예외 추가

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


}



