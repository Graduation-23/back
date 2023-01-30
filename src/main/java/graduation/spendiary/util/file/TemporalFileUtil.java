package graduation.spendiary.util.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class TemporalFileUtil {
    @Value("${path.tempFilesDir}")
    private static String PATH_TEMP_FILES_DIR;

    /**
     * 지정된 임시 폴더에 전송받은 파일을 임시 저장한다.
     * @param file 전송받은 MultipartFile
     * @return 저장된 임시 파일 경로.
     * @throws IOException
     */
    public static Path save(MultipartFile file) throws IOException {
        String oldName, newName;
        Path filePath;
        List<Path> saveFilePaths = Collections.emptyList();

        Files.createDirectories(Path.of(PATH_TEMP_FILES_DIR));

        // 새 이름은 "(UUID).(기존 확장자)" 형식
        oldName = file.getOriginalFilename();
        newName = UUID.randomUUID().toString() + oldName.substring(oldName.lastIndexOf("."));
        filePath = Paths.get(PATH_TEMP_FILES_DIR, newName);

        // 받은 파일을 임시 폴더에 저장
        file.transferTo(filePath);

        return filePath;
    }
}
