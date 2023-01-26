package graduation.spendiary.domain.diary;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryRepository extends MongoRepository<Diary, Long> {

//    public Diary save(Diary diary) {
//        diary.setDiaryId(++sequence);
//        store.put(diary.getDiaryId(), diary);
//        return diary;
//    }
//
//    public Diary findById(Long diaryId){
//        return store.get(diaryId);
//    }
//
//    public List<Diary> findAll() {
//        return new ArrayList<>(store.values());
//    }
//
//    public void update(Long diaryId, Diary updateParam) {
//        Diary findDiary = findById(diaryId);
//        findDiary.setDiaryTitle(updateParam.getDiaryTitle());
//        findDiary.setDiaryContent(updateParam.getDiaryContent());
//        findDiary.setDiaryImage(updateParam.getDiaryImage());
//        findDiary.setDiaryWeather(updateParam.getDiaryWeather());
//    }
//
//    public void clearStore() {
//        store.clear();
//    }
}
