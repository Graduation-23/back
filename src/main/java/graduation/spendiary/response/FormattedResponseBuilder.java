package graduation.spendiary.response;

import lombok.Data;
import lombok.Getter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;

@Data
@Getter
public class FormattedResponseBuilder {
    private ServerHttpRequest request;
    private ServerHttpResponse response;
    private Object body;
    private String service,accessToken, refreshToken;

    public FormattedResponseBuilder setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public FormattedResponseBuilder setBody(Object body) {
        this.body = body;
        return this;
    }

    public FormattedResponseBuilder setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public FormattedResponseBuilder setRequest(ServerHttpRequest request) {
        this.request = request;
        return this;
    }

    public FormattedResponseBuilder setResponse(ServerHttpResponse response) {
        this.response = response;
        return this;
    }

    public FormattedResponseBuilder setService(String service) {
        this.service = service;
        return this;
    }

    public FormattedResponse build() {
        return new FormattedResponse(this);
    }

//
//    public static <T> FormattedClientHttpResponse<T> from(ServiceType serviceType, String path, boolean ok, T value) {
//        return new FormattedClientHttpResponse<T>(serviceType, path, ok, value, Collections.emptyMap());
//    }
//
//    public static <T> FormattedClientHttpResponse<T> from(
//            ServiceType serviceType, String path, boolean ok, T value, String accessToken
//    ) {
//        return new FormattedClientHttpResponse<T>(serviceType, path, ok, value, Map.of("access-token", accessToken));
//    }
//
//    public static <T> FormattedClientHttpResponse<T> from(
//            ServiceType serviceType, String path, boolean ok, T value, String accessToken, String refreshToken
//    ) {
//        return new FormattedClientHttpResponse<T>(serviceType, path, ok, value,
//                Map.of("access-token", accessToken, "refresh-token", refreshToken));
//    }
}
