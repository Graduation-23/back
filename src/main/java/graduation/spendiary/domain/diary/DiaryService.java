package graduation.spendiary.domain.diary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiaryService {
    @Autowired
    private DiaryRepository repo;

    public List<Diary> getAll() {
        return repo.findAll();
    }

    public Diary getById(long id) {
        return repo.findById(id).get();
    }

    public void save(Diary diary) {
        repo.save(diary);
    }
}
