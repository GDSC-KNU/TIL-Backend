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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public String signup(UserSignupRequestDto request) {
        boolean existMember = userRepository.existsByAccount(request.getAccount());

        // 이미 회원이 존재하는 경우
        if (existMember) return null;

        return userRepository.save(User.builder()
                .account(request.getAccount())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .build()).getAccount();
    }
    public String login(JwtRequestDto request) throws Exception {
        User user = userRepository.findByAccount(request.getAccount())
                .orElseThrow(()-> new IllegalArgumentException("Not signed up User"));
        if(!passwordEncoder.matches(request.getPassword(),user.getPassword())){
            throw new IllegalArgumentException("Wrong Password");
        }
        return jwtTokenProvider.createToken(user.getAccount(), user.getRole());


    }
}
