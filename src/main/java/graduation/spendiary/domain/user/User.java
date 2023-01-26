package graduation.spendiary.domain.user;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "User")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    private Long    user_id;
    private String  user_name;
    private String  user_nickname;
    private String  user_email;
    private String  user_password;
    private String  user_access_token;
    private String  user_refresh_token;
}
