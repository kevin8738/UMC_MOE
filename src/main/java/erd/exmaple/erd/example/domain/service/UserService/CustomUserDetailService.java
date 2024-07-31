
//import erd.exmaple.erd.example.domain.UserEntity;
//import erd.exmaple.erd.example.domain.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Service
//public class CustomUserDetailService implements UserDetailsService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
//        // 전화번호를 사용하여 사용자 정보를 데이터베이스에서 조회합니다.
//        UserEntity user = userRepository.findByPhoneNumber(phoneNumber);
//        if (user == null) {
//            throw new UsernameNotFoundException("User not found");
//        }

//         조회된 사용자 정보를 사용하여 UserDetails 객체를 생성합니다.
//        return org.springframework.security.core.userdetails.User
//                .withUsername(user.getPhoneNumber())
//                .password(user.getPassword())
//                .authorities("USER") // 사용자의 권한을 설정합니다.
//                .build();
//    }
//}
