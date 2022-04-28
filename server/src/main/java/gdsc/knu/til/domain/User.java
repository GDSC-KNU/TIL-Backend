package gdsc.knu.til.model.entity;
import gdsc.knu.til.dto.UserSignupRequestDto;
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

    private Role

    public User(UserSignupRequestDto request){
        account = request.getAccount();
        password = request.getPassword();
        createdAt = LocalDateTime.now();
    }

    public void encryptPassword(PasswordEncoder passwordEncoder){

        password = passwordEncoder.encode(password);
    }
}