package gdsc.knu.til.service;

import gdsc.knu.til.dto.JwtRequestDto;
import gdsc.knu.til.dto.UserSignupRequestDto;
import gdsc.knu.til.domain.User;
import gdsc.knu.til.model.Role;
import gdsc.knu.til.repository.UserRepository;
import gdsc.knu.til.security.UserDetailsImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public String signup(UserSignupRequestDto request) {
        boolean existMember = userRepository.existsById(request.getAccount());

        // 이미 회원이 존재하는 경우
        if (existMember) return null;

        User user = new User(request);
        user.encryptPassword(passwordEncoder);

        userRepository.save(user);
        return user.getAccount();
    }
    public String login(JwtRequestDto request) throws Exception {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getAccount(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        return principal.getUser();
    }
}
