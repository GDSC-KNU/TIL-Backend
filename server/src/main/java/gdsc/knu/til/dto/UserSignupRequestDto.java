package gdsc.knu.til.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserSignupRequestDto {

    private String account;

    private String password;

}
