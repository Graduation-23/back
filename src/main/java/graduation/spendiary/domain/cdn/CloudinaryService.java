package graduation.spendiary.domain.cdn;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;

/**
 * CDN 서버 'CloudinaryService'와 통신하기 위한 클래스
 * @author Bonwoong Ku
 */
@Component
public class CloudinaryService {
    private static Cloudinary cloudinary = null;

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
        Map options = ObjectUtils.asMap(
                "use_filename", true,
                "unique_filename", false
        );
        Map map = cloudinary.uploader().upload(new File(path.toString()), options);
        return (String) map.get("secure_url");
    }
}
