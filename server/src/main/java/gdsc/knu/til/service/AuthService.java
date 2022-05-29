package gdsc.knu.til.service;

import gdsc.knu.til.domain.Role;
import gdsc.knu.til.domain.User;
import gdsc.knu.til.dto.JwtRequestDto;
import gdsc.knu.til.dto.UserSignupRequestDto;
import gdsc.knu.til.exception.NotExistsUserException;
import gdsc.knu.til.provider.JwtTokenProvider;
import gdsc.knu.til.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .build()).getAccount() ;
    }
    public String login(JwtRequestDto request) throws Exception {
        User user = userRepository.findByAccount(request.getAccount())
                .orElseThrow(()-> new UsernameNotFoundException("Not signed up User"));
        
        if(!passwordEncoder.matches(request.getPassword(),user.getPassword())){
            throw new NotExistsUserException();
        }
        
        return jwtTokenProvider.createToken(user.getAccount(), user.getRole());
    }

}
