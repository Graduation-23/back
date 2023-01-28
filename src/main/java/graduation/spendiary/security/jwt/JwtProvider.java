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

    private final Long expiryTimeMs;

    private final String secretKey;

    private Key key;

    private final String AUTHORITIES_KEY = "userId";

    public JwtProvider(@Value("${jwt.secretKey}") String secretKey, @Value("${jwt.expiryTimeMs}") Long expiryTimeMs){
        this.secretKey = secretKey;
        this.expiryTimeMs = expiryTimeMs;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // JWT Access 토큰 생성
    public String issueAccessToken(Authentication authentication) {
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
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(claims.get("userId"), token, authorities);
    }

    /**token 유효성 검증 */
    public boolean validateToken(String token){
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
