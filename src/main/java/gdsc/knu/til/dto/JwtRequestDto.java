package gdsc.knu.til.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class JwtRequestDto {

    // private Long id;

    private String account;

    private String password;

    // private LocalDateTime createdAt;
}
