package graduation.spendiary.domain.diary;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.List;

@Document(collection = "DiaryContents")
@Getter @Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED) //기본 생성자 생성
public class Diary {

    @Field("diary_id")
    private Long id;
    
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
}
