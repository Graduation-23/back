package graduation.spendiary.domain.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mongodb.lang.Nullable;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Document(collection = "User")
@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @MongoId
    private String  id;

    @Field("user_nickname")
    private String  nickname;

    @Field("user_password")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String  password;

    @Field("user_refresh_token")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String  refreshToken;

    @Field("user_access_type")
    private String accessType;

    @Field("user_create")
    private LocalDate created;

    @Field("user_birth")
    @DateTimeFormat(pattern = "YYYY-MM-DD")
    private LocalDate birth;

    @Field("user_profile_pic_url")
    private String profilePicUrl;
}
