package graduation.spendiary.domain.diary;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DiaryRepository {
    private static final Map<Long, Diary> store = new HashMap<>();
    private static long sequence = 0L;

    public Diary save(Diary diary) {
        diary.setDiaryId(++sequence);
        store.put(diary.getDiaryId(), diary);
        return diary;
    }

    public Diary findById(Long diaryId){
        return store.get(diaryId);
    }

    public List<Diary> findAll() {
        return new ArrayList<>(store.values());
    }

    public void update(Long diaryId, Diary updateParam) {
        Diary findDiary = findById(diaryId);
        findDiary.setDiaryTitle(updateParam.getDiaryTitle());
        findDiary.setDiaryContent(updateParam.getDiaryContent());
        findDiary.setDiaryImage(updateParam.getDiaryImage());
        findDiary.setDiaryWeather(updateParam.getDiaryWeather());
    }

    public void clearStore() {
        store.clear();
    }
}
