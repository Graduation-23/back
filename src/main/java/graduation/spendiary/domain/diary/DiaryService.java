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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DiaryService {
    private static final Pattern NEW_IMAGE_ID_PLACEHOLDER_PATTERN = Pattern.compile("^\\$(\\d+)$");

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

    public Diary save(DiarySaveVo vo, String userId)
        throws IOException
    {
        List<String> fileNames = uploadImages(vo.getImages());

        Diary diary = Diary.builder()
                .title(vo.getTitle())
                .content(vo.getContent())
                .user(userId)
                .images(fileNames)
                .weather(vo.getWeather())
                .build();
        diary.setId(SequenceGeneratorService.generateSequence(Diary.SEQUENCE_NAME));

        repo.save(diary);
        return diary;
    }

    public List<Diary> getByCreatedDateRange(String userId, LocalDate start, LocalDate end) {
        return repo.findByUserAndCreatedBetween(userId, start, end);
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

    /**
     * diary를 수정합니다.
     * @param diaryId 수정할 diary ID
     * @param vo diary 수정 정보
     * @param userId 사용자 ID
     * @return 수정된 diary
     * @throws NumberFormatException
     * @throws IndexOutOfBoundsException
     * @throws IOException
     */
    public Diary edit(long diaryId, DiaryEditVo vo, String userId)
            throws NumberFormatException, IndexOutOfBoundsException, IOException {
        List<String> fileNames = uploadImages(vo.getNewImages());

        List<String> newImageIds = new ArrayList<>(vo.getImageIds());

        // vo.images()의 "$i"를 fileNames로 대체
        for (String id: newImageIds) {
            Matcher matcher = NEW_IMAGE_ID_PLACEHOLDER_PATTERN.matcher(id);
            if (matcher.find()) {
                int value = Integer.parseInt(matcher.group(1));
                id = fileNames.get(value);
            }
        }

        Diary diary = Diary.builder()
                .id(diaryId)
                .title(vo.getTitle())
                .content(vo.getContent())
                .user(userId)
                .images(newImageIds)
                .build();

        repo.save(diary);

        return diary;
    }

    /**
     * 이미지 파일들을 임의의 이름으로 CDN 서버에 업로드합니다.
     * @param images 업로드할 이미지 파일들
     * @return 모든 이미지 업로드에 성공 시 재설정된 파일의 이름 리스트
     * @throws IOException 임시 폴더 또는 CDN 서버에 파일 저장 실패
     */
    private List<String> uploadImages(List<MultipartFile> images)
        throws IOException
    {
        Path path;
        List<String> fileNames = new ArrayList<>();
        for (MultipartFile file: images) {
            path = temporalFileUtil.save(file);
            cloudinaryService.upload(path);
            fileNames.add(path.getFileName().toString());
        }
        return fileNames;
    }
}
