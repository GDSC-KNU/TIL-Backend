package gdsc.knu.til.service;

import gdsc.knu.til.dto.UserSignupRequestDto;
import gdsc.knu.til.model.entity.User;
import gdsc.knu.til.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public String signup(UserSignupRequestDto request) {
        boolean existMember = userRepository.existsById(request.getAccount());

        // 이미 회원이 존재하는 경우
        if (existMember) return null;

        User user = new User(request);
        user.encryptPassword(passwordEncoder);

        userRepository.save(user);
        return user.getAccount();
    }}
