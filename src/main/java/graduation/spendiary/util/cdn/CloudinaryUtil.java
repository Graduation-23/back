package graduation.spendiary.util.cdn;

import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

public class CloudinaryUtil {
    @Value("${cloudinary.cloudName}")
    private static String CLOUD_NAME;
    @Value("${cloudinary.apiKey}")
    private static String API_KEY;
    @Value("${cloudinary.apiSecret}")
    private static String API_SECRET;

    // singleton
    protected static final CloudinaryUtil instance = new CloudinaryUtil();

    protected CloudinaryUtil() {
        Map config = Map.of(
                "cloud_name", CLOUD_NAME,
                "api_key", API_KEY,
                "api_secret", API_SECRET
        );

    }

    public static CloudinaryUtil getInstance() {
        return instance;
    }
}
