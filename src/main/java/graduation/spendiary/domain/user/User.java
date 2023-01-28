package graduation.spendiary.domain.user;

import com.mongodb.lang.Nullable;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "User")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @MongoId
    @Field("user_id")
    private String  id;
    @Field("user_nickname")
    private String  nickname;
    @Field("user_email")
    private String  email;
    @Field("user_password")
    private String  password;
    @Field("user_access_token")
    @Nullable
    private String  accessToken;
    @Field("user_refresh_token")
    @Nullable
    private String  refreshToken;
}
