package graduation.spendiary.domain.cdn;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileWriter;
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
            "unique_filename", true
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
     * MultipartFile을 Cloudinary에 업로드합니다.
     * @param file 업로드할 파일
     * @throws IOException
     * @return Cloudinary에 업로드된 파일의 URL (https)
     */
    public String upload(MultipartFile file) throws IOException {
        Map response = cloudinary.uploader().upload(file.getBytes(), UPLOAD_OPTIONS);
        file.transferTo(new File("C:/Temp/Paiary/" + file.getOriginalFilename())); // test code
        return (String) response.get("secure_url");
    }

    /**
     * 다수의 MultipartFile을 Cloudinary에 업로드합니다.
     * @param files 업로드할 파일 리스트
     * @throws IOException
     * @return Cloudinary에 업로드된 파일의 URL 리스트 (https)
     */
    public List<String> upload(List<MultipartFile> files) throws IOException {
        List<String> urls = new ArrayList<>();
        for (MultipartFile file: files) {
            urls.add(this.upload(file));
        }
        return urls;
    }
}
