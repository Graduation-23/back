package graduation.spendiary.domain.diary;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class DiaryRepositoryTest {

    DiaryRepository diaryRepository = new DiaryRepository();
    LocalDate currentDate = LocalDate.now();
    List<String> diaryImage = Arrays.asList("image1", "image2");
    @AfterEach
    void afterEach() {
        diaryRepository.clearStore();
    }

    @Test
    void save() {
        Diary diary = new Diary("Title", "Contents", currentDate, diaryImage, "sunny" );

        Diary saveDiary = diaryRepository.save(diary);

        Diary findDiary = diaryRepository.findById(diary.getDiaryId());
        assertThat(findDiary).isEqualTo(saveDiary);
    }

    @Test
    void findAll() {
        Diary diary1 = new Diary("잠온다", "내용 바뀌기 전", currentDate, diaryImage, "sunny" );
        Diary diary2 = new Diary("집가고 싶다", "내용 바뀜", currentDate, diaryImage, "rainy" );

        diaryRepository.save(diary1);
        diaryRepository.save(diary2);

        List<Diary> result = diaryRepository.findAll();

        assertThat(result.size()).isEqualTo(2);
        assertThat(result).contains(diary1,diary2);
    }

    @Test
    void updateDiary() {
        Diary diary1 = new Diary("잠온다", "내용 바뀌기 전", currentDate, diaryImage, "sunny" );

        Diary savedItem = diaryRepository.save(diary1);
        Long diaryId = savedItem.getDiaryId();

        Diary updateParam = new Diary("집가고 싶다", "내용 바뀜", currentDate, diaryImage, "rainy" );
        diaryRepository.update(diaryId, updateParam);

        Diary findDiary = diaryRepository.findById(diaryId);
        assertThat(findDiary.getDiaryTitle()).isEqualTo(updateParam.getDiaryTitle());
    }

}