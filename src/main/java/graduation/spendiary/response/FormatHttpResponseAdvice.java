package graduation.spendiary.response;

import graduation.spendiary.security.jwt.JwtAuthenticationFilter;
import org.aopalliance.aop.Advice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.List;

@ControllerAdvice
public class FormatHttpResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        if (returnType.getContainingClass().getPackageName().contains("graduation.spendiary")
                && converterType != StringHttpMessageConverter.class) {
            return true;
        }
        return false;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        List access = response.getHeaders().get(JwtAuthenticationFilter.ACCESS_HEADER_KEY),
            refresh = response.getHeaders().get(JwtAuthenticationFilter.REFRESH_HEADER_KEY);
        return new FormattedResponseBuilder()
                .setResponse(response)
                .setRequest(request)
                .setBody(body)
                .setAccessToken((access != null && access.size() > 0) ? String.valueOf(access.get(0)) : null)
                .setRefreshToken((refresh != null && refresh.size() > 0) ? String.valueOf(refresh.get(0)) : null)
                .setService(returnType.getContainingClass().getSimpleName())
                .build();
    }
}
