package graduation.spendiary.service;

import graduation.spendiary.domain.Diary;
import graduation.spendiary.repository.DiaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
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
