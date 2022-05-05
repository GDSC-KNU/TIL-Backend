package gdsc.knu.til.model;
import gdsc.knu.til.dto.UserSignupRequestDto;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String account;

    private String password;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Override
    public String toString() {
        return "User{" +
                "account='" + account + '\'' +
                ", role=" + role +
                '}';
    }
}