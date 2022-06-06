package gdsc.knu.til.service;

import gdsc.knu.til.domain.User;
import gdsc.knu.til.dto.JwtRequestDto;
import gdsc.knu.til.dto.UserSignupRequestDto;
import gdsc.knu.til.provider.JwtTokenProvider;
import gdsc.knu.til.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.time.LocalDateTime;

@Service
@Transactional
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public String signup(UserSignupRequestDto request) {
        boolean existMember = userRepository.existsByAccount(request.getAccount());

        // 이미 회원이 존재하는 경우
        if (existMember) throw new KeyAlreadyExistsException();

        return userRepository.save(User.builder()
                .account(request.getAccount())
                .password(passwordEncoder.encode(request.getPassword()))
                .createdAt(LocalDateTime.now())
                .build()).getAccount() ;
    }
    public String login(JwtRequestDto request)  {
        User user = userRepository.findByAccount(request.getAccount())
                .orElseThrow(()-> new BadCredentialsException("아이디 혹은 비밀번호가 잘못되었습니다."));
        
        if(!passwordEncoder.matches(request.getPassword(),user.getPassword())){
            throw new BadCredentialsException("아이디 혹은 비밀번호가 잘못되었습니다.");
        }
        
        return jwtTokenProvider.createToken(user.getAccount());
    }

}
