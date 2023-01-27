package graduation.spendiary.util.responseFormat;

import graduation.spendiary.security.jwt.Token;
import lombok.Data;

import java.util.Collections;
import java.util.Map;

@Data
public class ResponseFormat<T> {
    public final ServiceType serviceType;
    public final String path;
    public final boolean ok;
    public final T value;
    public final Map<String, String> token;

    public static <T> ResponseFormat<T> from(ServiceType serviceType, String path, boolean ok, T value) {
        return new ResponseFormat<T>(serviceType, path, ok, value, Collections.emptyMap());
    }

    public static <T> ResponseFormat<T> from(
            ServiceType serviceType, String path, boolean ok, T value, Token accessToken
    ) {
        return new ResponseFormat<T>(serviceType, path, ok, value, Map.of("access-token", accessToken.getToken()));
    }

    public static <T> ResponseFormat<T> from(
            ServiceType serviceType, String path, boolean ok, T value, Token accessToken, Token refreshToken
    ) {
        return new ResponseFormat<T>(serviceType, path, ok, value,
                Map.of("access-token", accessToken.getToken(), "refresh-token", refreshToken.getToken()));
    }
}
