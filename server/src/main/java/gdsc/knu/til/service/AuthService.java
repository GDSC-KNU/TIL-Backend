package gdsc.knu.til.service;

import gdsc.knu.til.dto.JwtRequestDto;
import gdsc.knu.til.dto.UserSignupRequestDto;
import gdsc.knu.til.model.Role;
import gdsc.knu.til.model.User;
import gdsc.knu.til.provider.JwtTokenProvider;
import gdsc.knu.til.repository.UserRepository;
import gdsc.knu.til.security.UserDetailsImpl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.time.LocalDateTime;

@Service
@Transactional
@AllArgsConstructor
@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public ResponseEntity<?> signup(UserSignupRequestDto request) {
        boolean existMember = userRepository.existsByAccount(request.getAccount());

        // 이미 회원이 존재하는 경우
        if (existMember) return new ResponseEntity<>("Already Exist User",HttpStatus.CONFLICT);

        return new ResponseEntity<>(userRepository.save(User.builder()
                .account(request.getAccount())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .build()).getAccount(),HttpStatus.OK) ;
    }
    public ResponseEntity<?> login(JwtRequestDto request) throws Exception {
        boolean existMember = userRepository.existsByAccount(request.getAccount());
        if (!existMember) return new ResponseEntity<>("No Any User",HttpStatus.BAD_REQUEST);

        User user = userRepository.findByAccount(request.getAccount())
                .orElseThrow(()->new IllegalArgumentException(""));
        if(!passwordEncoder.matches(request.getPassword(),user.getPassword())){
            return new ResponseEntity<>("Wrong Password", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(jwtTokenProvider.createToken(user.getAccount(), user.getRole()),HttpStatus.OK);


    }

}
