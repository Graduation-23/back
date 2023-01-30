package graduation.spendiary.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtProvider implements InitializingBean {

    private final Long refreshExpiryTimeMs, accessExpiryTimeMs;

    private final Key accessTokenKey, refreshTokenKey;

    private final String AUTHORITIES_KEY = "userId";

    public JwtProvider(
            @Value("${jwt.accessSecretKey}") String accessSecretKey,
            @Value("${jwt.accessExpiryTimeMs}") Long accessExpiryTimeMs,
            @Value("${jwt.refreshSecretKey}") String refreshSecretKey,
            @Value("${jwt.refreshExpiryTimeMs}") Long refreshExpiryTimeMs
    ){
        this.accessExpiryTimeMs = accessExpiryTimeMs;
        this.refreshExpiryTimeMs = refreshExpiryTimeMs;

        this.accessTokenKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(accessSecretKey));
        this.refreshTokenKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(refreshSecretKey));
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    public Authentication makeAuthentication(String id) {
        return new UsernamePasswordAuthenticationToken(id, null, null);
    }

    // JWT Access 토큰 생성
    public String issueAccessToken(Authentication authentication) {
        return this.issueToken(authentication, accessTokenKey, accessExpiryTimeMs);
    }

    public String issueRefreshToken(Authentication authentication) {
        return this.issueToken(authentication, refreshTokenKey, refreshExpiryTimeMs);
    }

    public Authentication getAccessAuthentication(String token) {
        return this.getAuthentication(token, accessTokenKey);
    }

    public Authentication getRefreshAuthentication(String token) {
        return this.getAuthentication(token, refreshTokenKey);
    }

    public boolean validateAccessToken(String token){
        return this.validateToken(token, accessTokenKey);
    }

    public boolean validateRefreshToken(String token){
        return this.validateToken(token, refreshTokenKey);
    }

    private String issueToken(Authentication authentication, Key key, Long expiryTimeMs) {
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .claim(AUTHORITIES_KEY, authentication.getPrincipal())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expiryTimeMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**인증 정보 조회 */
    public Authentication getAuthentication(String token, Key key) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(claims.get(AUTHORITIES_KEY), token, authorities);
    }

    /**token 유효성 검증 */
    public boolean validateToken(String token, Key key){
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        }catch(io.jsonwebtoken.security.SecurityException | MalformedJwtException e){
            System.out.println("잘못된 JWT 서명입니다.");
        }catch(ExpiredJwtException e){
            System.out.println("만료된 JWT 토큰입니다.");
        }catch(UnsupportedJwtException e){
            System.out.println("지원하지 않는 JWT 토큰입니다.");
        }catch(IllegalArgumentException e){
            System.out.println("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }
}
