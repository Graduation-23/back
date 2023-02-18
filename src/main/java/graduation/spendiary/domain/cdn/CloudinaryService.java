package graduation.spendiary.domain.cdn;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * CDN 서버 'CloudinaryService'와 통신하기 위한 클래스
 * @author Bonwoong Ku
 */
@Component
public class CloudinaryService {
    private static Cloudinary cloudinary = null;
    private static final Map UPLOAD_OPTIONS = ObjectUtils.asMap(
            "use_filename", true,
            "unique_filename", false
    );

    public CloudinaryService(
            @Value("${cloudinary.cloudName}")   final String CLOUD_NAME,
            @Value("${cloudinary.apiKey}")      final String API_KEY,
            @Value("${cloudinary.apiSecret}")   final String API_SECRET
    ) {
        try {
            Map config = ObjectUtils.asMap(
                    "cloud_name", CLOUD_NAME,
                    "api_key", API_KEY,
                    "api_secret", API_SECRET
            );
            cloudinary = new Cloudinary(config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 로컬 경로에 위치한 파일을 Cloudinary에 업로드합니다.
     * @param path 파일의 로컬 경로
     * @throws IOException
     * @return Cloudinary에 업로드된 파일의 URL (https)
     */
    public String upload(Path path) throws IOException {
        Map response = cloudinary.uploader().upload(new File(path.toString()), UPLOAD_OPTIONS);
        return (String) response.get("secure_url");
    }

    public String upload(MultipartFile file) throws IOException {
        Map response = cloudinary.uploader().upload(file.getBytes(), UPLOAD_OPTIONS);
        return (String) response.get("secure_url");
    }

    public List<String> upload(List<MultipartFile> files) throws IOException {
        List<String> urls = new ArrayList<>();
        for (MultipartFile file: files) {
            urls.add(this.upload(file));
        }
        return urls;
    }
}
