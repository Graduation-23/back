package graduation.spendiary.domain.cdn;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CloudinaryService {
    @Value("${cloudinary.cloudName}")
    private static String CLOUD_NAME;
    @Value("${cloudinary.apiKey}")
    private static String API_KEY;
    @Value("${cloudinary.apiSecret}")
    private static String API_SECRET;

    protected CloudinaryService() {
        Map config = Map.of(
                "cloud_name", CLOUD_NAME,
                "api_key", API_KEY,
                "api_secret", API_SECRET
        );
        Cloudinary cloudinary = new Cloudinary(config);
    }
}
