package graduation.spendiary.domain.user;

import com.mongodb.lang.Nullable;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDate;

@Document(collection = "User")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @MongoId
    @Field("_id")
    private String  id;
    @Field("user_nickname")
    private String  nickname;
    @Field("user_password")
    @Nullable
    private String  password;
    @Field("user_refresh_token")
    @Nullable
    private String  refreshToken = "";
    @Field("user_access_type")
    @Nullable
    private String accessType = "none";
    @CreatedDate
    @Field("user_createdAt")
    private LocalDate createdAt;
}
