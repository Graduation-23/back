package graduation.spendiary.domain.user;

import lombok.Data;

@Data
public class UserDTO {
    private String id;
    private String nickname;
    private String birth;
    private String accessType;
}
