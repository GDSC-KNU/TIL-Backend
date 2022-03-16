package gdsc.knu.til.model.network.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserApiResponse {

    private Long id;

    private String account;

    private String password;

    private LocalDateTime createdAt;

}
