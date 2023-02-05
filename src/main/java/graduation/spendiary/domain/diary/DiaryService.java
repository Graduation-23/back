package graduation.spendiary.domain.diary;

import graduation.spendiary.domain.DatabaseSequence.SequenceGeneratorService;
import graduation.spendiary.domain.cdn.CloudinaryService;
import graduation.spendiary.domain.user.User;
import graduation.spendiary.security.jwt.Authorization;
import graduation.spendiary.util.file.TemporalFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DiaryService {
    @Autowired
    private DiaryRepository repo;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private TemporalFileUtil temporalFileUtil;

    public List<Diary> getAll() {
        return repo.findAll();
    }

    public Diary getById(long id) {
        return repo.findById(id).get();
    }

    public void save(Diary diary) {
        repo.save(diary);
    }

    public Optional<Diary> save(DiarySaveVo vo, String userId)  {
        Path path;
        List<String> fileNames = new ArrayList<>();
        for (MultipartFile file: vo.getImages()) {
            try {
                path = temporalFileUtil.save(file);
                cloudinaryService.upload(path);
                fileNames.add(path.getFileName().toString());
            }
            catch (IOException e) {
                return Optional.empty();
            }
        }

        Diary diary = Diary.builder()
                .title(vo.getTitle())
                .content(vo.getContent())
                .user(userId)
                .images(fileNames)
                .weather(vo.getWeather())
                .build();
        diary.setId(SequenceGeneratorService.generateSequence(Diary.SEQUENCE_NAME));

        repo.save(diary);

        return Optional.of(diary);
    }
}
