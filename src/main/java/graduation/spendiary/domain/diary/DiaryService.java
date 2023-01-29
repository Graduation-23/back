package graduation.spendiary.domain.diary;

import graduation.spendiary.util.file.TemporalFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class DiaryService {
    @Value("${path.tempFilesDir}")
    private String PATH_TEMP_FILES_DIR;

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

    public boolean save(DiarySaveVo vo)  {
        List<Path> paths = TemporalFileUtil.save(vo.getImages());
    }
}
