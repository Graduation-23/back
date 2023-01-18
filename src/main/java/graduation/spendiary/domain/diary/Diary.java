package graduation.spendiary.domain.diary;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //기본 생성자 생성
public class Diary {

    private Long diaryId;

    private String diaryTitle;

    private String diaryContent;

    private LocalDate diaryCreate;

    private List<String> diaryImage;;

    private String diaryWeather;

    public Diary(String diaryTitle, String diaryContent, LocalDate diaryCreate, List<String> diaryImage, String diaryWeather) {
        this.diaryTitle = diaryTitle;
        this.diaryContent = diaryContent;
        this.diaryCreate = diaryCreate;
        this.diaryImage = diaryImage;
        this.diaryWeather = diaryWeather;
    }
}
