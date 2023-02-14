package graduation.spendiary.domain.diary;

import graduation.spendiary.domain.DatabaseSequence.SequenceGeneratorService;
import graduation.spendiary.domain.cdn.CloudinaryService;
import graduation.spendiary.domain.spendingWidget.SpendingWidgetDto;
import graduation.spendiary.domain.spendingWidget.SpendingWidgetService;
import graduation.spendiary.exception.DiaryDuplicatedException;
import graduation.spendiary.exception.DiaryUneditableException;
import graduation.spendiary.exception.NoSuchContentException;
import graduation.spendiary.util.file.TemporalFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class DiaryService {
    private static final Pattern NEW_IMAGE_ID_PLACEHOLDER_PATTERN = Pattern.compile("^\\$(\\d+)$");

    @Autowired
    private DiaryRepository repo;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private TemporalFileUtil temporalFileUtil;
    @Autowired
    private SpendingWidgetService widgetService;

    public DiaryDto getDto(Diary diary) {
        DiaryDto.DiaryDtoBuilder builder = DiaryDto.builder()
                .id(diary.getId())
                .title(diary.getTitle())
                .content(diary.getContent())
                .thumbnailIdx(diary.getThumbnailIdx())
                .imageUrls(diary.getImageUrls())
                .date(diary.getDate())
                .weather(diary.getWeather());
        try {
            builder = builder.widget(widgetService.getDtoByDiaryId(diary.getId()));
        }
        catch (NoSuchContentException | NullPointerException e) {
            builder = builder.widget(null);
        }
        return builder.build();
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
                .weather("")
                .build();
        diary.setId(SequenceGeneratorService.generateSequence(Diary.SEQUENCE_NAME));

        Diary savedDiary = repo.save(diary);
        return savedDiary.getId();
    }

    public List<DiaryDto> getDtoByCreatedRange(String userId, LocalDate start, LocalDate end) {
        return repo.findByUserAndCreatedBetween(userId, start, end).stream()
                .map(this::getDto)
                .collect(Collectors.toList());
    }

    public List<DiaryDto> getOfLastWeek(String userId) {
        LocalDate now = LocalDate.now();
        LocalDate lastWeek = now.minusWeeks(1);
        return getDtoByCreatedRange(userId, lastWeek, now);
    }

    public List<DiaryDto> getOfLastMonth(String userId) {
        LocalDate now = LocalDate.now();
        LocalDate lastMonth = now.minusMonths(1);
        return getDtoByCreatedRange(userId, lastMonth, now);
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
        return getDtoByCreatedRange(userId, firstDay, lastDay);
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
        return getDtoByCreatedRange(userId, firstDay, lastDay);
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

        // 새 이미지들을 업로드
        List<String> uploadedImageUrls = uploadImages(vo.getNewImages());

        // vo.images()의 "$i"를 uploadedImageUrl[i]로 대체
        List<String> newImageUrls = vo.getImageUrls().stream()
                .map(id -> {
                    Matcher matcher = NEW_IMAGE_ID_PLACEHOLDER_PATTERN.matcher(id);
                    if (matcher.find()) {
                        int idx = Integer.parseInt(matcher.group(1));
                        return uploadedImageUrls.get(idx);
                    }
                    return id;
                })
                .collect(Collectors.toList());

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

        // 위젯 저장
        SpendingWidgetDto widgetDto = vo.getWidget();
        if (widgetDto != null) {
            widgetDto.setDiaryId(newDiary.getId());
            widgetService.save(widgetDto);
        }

        // 다이어리 저장
        repo.save(newDiary);
        return this.getDto(newDiary);
    }

    /**
     * 이미지 파일들을 임의의 이름으로 CDN 서버에 업로드합니다.
     * @param images 업로드할 이미지 파일들
     * @return 모든 이미지 업로드에 성공 시 업로드 된 파일 URL (https) 리스트
     * @throws IOException 임시 폴더 또는 CDN 서버에 파일 저장 실패
     */
    private List<String> uploadImages(List<MultipartFile> images)
        throws IOException
    {
        Path path;
        String imageUrl;
        List<String> fileNames = new ArrayList<>();
        for (MultipartFile file: images) {
            path = temporalFileUtil.save(file);
            imageUrl = cloudinaryService.upload(path);
            fileNames.add(imageUrl);
        }
        return fileNames;
    }
}
