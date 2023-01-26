package graduation.spendiary.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Document(collection = "DiaryContents")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //기본 생성자 생성
public class Diary {

    private Long diary_id;

    private String diary_title;

    private String diary_content;

    @CreatedDate
    private LocalDate diary_create;

    private List<String> diary_image;;

    private String diary_weather;
}
