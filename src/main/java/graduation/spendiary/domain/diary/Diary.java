package graduation.spendiary.domain.diary;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.List;

@Document(collection = "DiaryContents")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED) //기본 생성자 생성
@AllArgsConstructor
@Builder
public class Diary {

    @Transient
    public static final String SEQUENCE_NAME = "diary_sequence";

    @Id
    @Field("diary_id")
    private Long id;

    @Field("user_id")
    private String user;

    @Field("diary_title")
    private String title;

    @Field("diary_content")
    private String content;

    @CreatedDate
    @Field("diary_create")
    private LocalDate created;

    @Field("diary_image")
    private List<String> images;

    @Field("diary_weather")
    private String weather;

    @Version
    private Integer version;
}
