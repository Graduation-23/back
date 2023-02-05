package graduation.spendiary.domain.diary;

import graduation.spendiary.domain.DatabaseSequence.SequenceGeneratorService;
import graduation.spendiary.domain.cdn.CloudinaryService;
import graduation.spendiary.util.file.TemporalFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
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

    public List<Diary> getByCreatedDateRange(String userId, LocalDate start, LocalDate end) {
        return Collections.emptyList(); // todo
    }

    public List<Diary> getOfLastWeek(String userId) {
        LocalDate now = LocalDate.now();
        LocalDate lastWeek = now.minusWeeks(1);
        return getByCreatedDateRange(userId, lastWeek, now);
    }

    public List<Diary> getOfLastMonth(String userId) {
        LocalDate now = LocalDate.now();
        LocalDate lastMonth = now.minusMonths(1);
        return getByCreatedDateRange(userId, lastMonth, now);
    }

    public List<Diary> getOfYear(String userId, int year) {
        LocalDate firstDay, lastDay;
        try {
            firstDay = LocalDate.of(year, 1, 1);
            lastDay = LocalDate.of(year, 12, 31);
        }
        catch (DateTimeException e) {
            return Collections.emptyList();
        }
        return getByCreatedDateRange(userId, firstDay, lastDay);
    }

    public List<Diary> getOfMonth(String userId, int year, int month) {
        LocalDate firstDay, lastDay;
        try {
            firstDay = LocalDate.of(year, month, 1);
            lastDay = LocalDate.of(year, month + 1, 1).minusDays(1);
        }
        catch (DateTimeException e) {
            return Collections.emptyList();
        }
        return getByCreatedDateRange(userId, firstDay, lastDay);
    }
}
