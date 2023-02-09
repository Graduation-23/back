package graduation.spendiary.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    public static final String ACCESS_HEADER_KEY = "Authorization", REFRESH_HEADER_KEY = "AuthorizationSecret";

    private final JwtProvider jwtProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String requestURI = httpServletRequest.getRequestURI();

        Authentication authentication = processAccessToken(httpServletRequest);

        if(authentication == null) authentication = processRefreshToken(httpServletRequest, (HttpServletResponse) response);

        if(authentication != null) SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(httpServletRequest, response);
    }

    private Authentication processAccessToken(HttpServletRequest httpServletRequest) {
        String token = resolveToken(httpServletRequest, ACCESS_HEADER_KEY);
        if(StringUtils.hasText(token) && jwtProvider.validateAccessToken(token)){
            return jwtProvider.getAccessAuthentication(token);
        }
        return null;
    }

    private Authentication processRefreshToken(HttpServletRequest httpServletRequest, HttpServletResponse response) {
        String token = resolveToken(httpServletRequest, REFRESH_HEADER_KEY);
        if(StringUtils.hasText(token) && jwtProvider.validateRefreshToken(token)){

            String userId = (String) jwtProvider.getRefreshAuthentication(token).getPrincipal();
            String issuedToken = jwtProvider.issueAccessToken(jwtProvider.makeAuthentication(userId));
            response.setHeader(ACCESS_HEADER_KEY, issuedToken);

            return jwtProvider.getAccessAuthentication(issuedToken);
        }
        return null;
    }


    private String resolveToken(HttpServletRequest request, String headerName){
        String bearerToken = request.getHeader(headerName);
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }
}
