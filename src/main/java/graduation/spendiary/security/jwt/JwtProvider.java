package graduation.spendiary.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtProvider {
    private static final String TOKEN_PREFIX = "Bearer ";

    @Value("${jwt.algorithm}")
    private String algorithm;

    @Value("${jwt.expiryTimeMs}")
    private Long expiryTimeMs;

    @Value("${jwt.secretKey}")
    private String secretKey;

    private Key key;

    // 객체 초기화, secretKey를 Base64로 인코딩한다.
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    // JWT Access 토큰 생성
    public String issueAccessToken(String userPk) {
        Claims claims = Jwts.claims().setSubject(userPk); // JWT payload 에 저장되는 정보단위
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expiryTimeMs))
                .signWith(key, SignatureAlgorithm.forName(algorithm))
                .compact();
    }

    // Authorization Header가 JWT인지 validate
    private void validateAuthorizationHeader(String header) {
        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            throw new IllegalArgumentException();
        }
    }

    private String extractToken(String authorizationHeader) {
        return authorizationHeader.substring(TOKEN_PREFIX.length());
    }

    public Claims parseToken(String authorizationHeader) {
        validateAuthorizationHeader(authorizationHeader);

        String token = extractToken(authorizationHeader);

        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
