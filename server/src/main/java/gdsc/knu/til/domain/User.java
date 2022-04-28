package gdsc.knu.til.domain;
import gdsc.knu.til.dto.UserSignupRequestDto;
import gdsc.knu.til.model.Role;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.net.PasswordAuthentication;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class User {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private String account;

    private String password;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private Role role;

    public User(UserSignupRequestDto request){
        account = request.getAccount();
        password = request.getPassword();
        createdAt = LocalDateTime.now();
        role = Role.USER; //Role 기본값

    }

    public void encryptPassword(PasswordEncoder passwordEncoder){

        password = passwordEncoder.encode(password);
    }

    @Override
    public String toString() {
        return "User{" +
                "account='" + account + '\'' +
                ", role=" + role +
                '}';
    }
}