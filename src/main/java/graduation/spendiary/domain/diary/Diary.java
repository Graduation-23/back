package graduation.spendiary.domain.diary;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

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

    @MongoId
    private Long id;

    @Field("user_id")
    private String user;

    @Field("diary_title")
    private String title;

    @Field("diary_content")
    private String content;

    @Field("diary_date")
    private LocalDate date;

    @Field("diary_image_urls")
    private List<String> imageUrls;

    @Field("diary_thumbnail_idx")
    private Long thumbnailIdx;

    @Field("diary_weather")
    private String weather;
}
