package graduation.spendiary.domain.diary;

import graduation.spendiary.domain.DatabaseSequence.SequenceGeneratorService;
import graduation.spendiary.domain.cdn.CloudinaryService;
import graduation.spendiary.exception.DiaryDuplicatedException;
import graduation.spendiary.exception.DiaryUneditableException;
import graduation.spendiary.exception.NoSuchContentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class DiaryService {
    private static final Pattern NEW_IMAGE_ID_PLACEHOLDER_PATTERN = Pattern.compile("^\\$(\\d+)$");

    @Autowired
    private DiaryRepository repo;
    @Autowired
    private CloudinaryService cloudinaryService;

    public DiaryDto getDto(Diary diary) {
        return DiaryDto.builder()
                .id(diary.getId())
                .title(diary.getTitle())
                .content(diary.getContent())
                .thumbnailIdx(diary.getThumbnailIdx())
                .imageUrls(diary.getImageUrls())
                .date(diary.getDate())
                .weather(diary.getWeather())
                .build();
    }

    public List<DiaryDto> getAllOfUser(String userId) {
        return repo.findByUser(userId).stream()
                .map(this::getDto)
                .collect(Collectors.toList());
    }

    public DiaryDto getDtoById(long id) {
        return this.getDto(repo.findById(id).get());
    }

    /**
     * 주어진 날짜에 해당되는 빈 다이어리를 작성합니다.
     * @param diaryDate 다이어리의 날짜
     * @param userId 다이어리 작성자
     * @return 빈 다이어리
     * @throws DiaryDuplicatedException 해당 날짜에 이미 다이어리가 있음
     */
    public Long saveEmptyDiary(String userId, LocalDate diaryDate)
        throws DiaryDuplicatedException
    {
        if (!repo.findByUserAndDate(userId, diaryDate).isEmpty())
            throw new DiaryDuplicatedException();

        Diary diary = Diary.builder()
                .title("")
                .content("")
                .user(userId)
                .imageUrls(Collections.emptyList())
                .date(diaryDate)
                .thumbnailIdx((long) -1)
                .weather("")
                .build();
        diary.setId(SequenceGeneratorService.generateSequence(Diary.SEQUENCE_NAME));

        Diary savedDiary = repo.save(diary);
        return savedDiary.getId();
    }

    public List<DiaryDto> getDtoByDateRange(String userId, LocalDate start, LocalDate end) {
        return repo.findByUserAndDateBetween(userId, start, end).stream()
                .map(this::getDto)
                .collect(Collectors.toList());
    }

    public List<DiaryDto> getOfLastWeek(String userId) {
        LocalDate now = LocalDate.now();
        LocalDate lastWeek = now.minusWeeks(1);
        return getDtoByDateRange(userId, lastWeek, now);
    }

    public List<DiaryDto> getOfLastMonth(String userId) {
        LocalDate now = LocalDate.now();
        LocalDate lastMonth = now.minusMonths(1);
        return getDtoByDateRange(userId, lastMonth, now);
    }

    public List<DiaryDto> getOfYear(String userId, int year) {
        LocalDate firstDay, lastDay;
        try {
            firstDay = LocalDate.of(year, 1, 1);
            lastDay = LocalDate.of(year, 12, 31);
        }
        catch (DateTimeException e) {
            return Collections.emptyList();
        }
        return getDtoByDateRange(userId, firstDay, lastDay);
    }

    public List<DiaryDto> getOfMonth(String userId, int year, int month) {
        LocalDate firstDay, lastDay;
        try {
            firstDay = LocalDate.of(year, month, 1);
            lastDay = LocalDate.of(year, month + 1, 1).minusDays(1);
        }
        catch (DateTimeException e) {
            return Collections.emptyList();
        }
        return getDtoByDateRange(userId, firstDay, lastDay);
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
    public DiaryDto edit(long diaryId, DiaryEditVo vo, String userId)
            throws NumberFormatException, IndexOutOfBoundsException, IOException {
        // 다이어리 가져오기
        Optional<Diary> oldDiaryOptional = repo.findById(diaryId);
        if (oldDiaryOptional.isEmpty())
            throw new NoSuchContentException();
        Diary oldDiary = oldDiaryOptional.get();

        // 생성된 시간보다 3일 초과해서 지났다면 안됨
        if (!Period.between(oldDiary.getDate(), LocalDate.now()).minusDays(3).isNegative())
            throw new DiaryUneditableException();

        List<String> newImageUrls = vo.getImageUrls() != null ? vo.getImageUrls() : new ArrayList<>();

        if(vo.getNewImages() != null) {
            // 새 이미지들을 업로드 후 url 등록
            newImageUrls.addAll(cloudinaryService.upload(vo.getNewImages()));
        }
        // diary entity 재생성
        Diary newDiary = Diary.builder()
                .id(diaryId)
                .title(vo.getTitle())
                .content(vo.getContent())
                .user(userId)
                .imageUrls(newImageUrls)
                .thumbnailIdx(vo.getThumbnailIdx())
                .date(oldDiary.getDate())
                .weather(vo.getWeather())
                .build();

        // 다이어리 저장
        repo.save(newDiary);
        return this.getDto(newDiary);
    }

    public boolean deleteDiary(String userId, Long diaryId) {
        Diary deleteDiary = repo.findById(diaryId).get();
        repo.deleteById(deleteDiary.getId());
        return true;
    }
}
