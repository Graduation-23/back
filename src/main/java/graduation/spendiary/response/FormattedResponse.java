package graduation.spendiary.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
public class FormattedResponse {

    public final String serviceType;
    public final String path;
    public final String method;
    public final LocalDateTime timestamp;
    public final Object data;
    public final Map<String, String> token = new HashMap<>();

    public FormattedResponse(FormattedResponseBuilder builder) {
        this.serviceType = builder.getService();
        this.path = builder.getRequest().getURI().getPath();
        this.timestamp = LocalDateTime.now();
        this.data = builder.getBody();
        this.method = builder.getRequest().getMethodValue();

        if(builder.getAccessToken() != null) this.token.put("access", builder.getAccessToken());
        if(builder.getRefreshToken() != null) this.token.put("refresh", builder.getRefreshToken());
    }

}
