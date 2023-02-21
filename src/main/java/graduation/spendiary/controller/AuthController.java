package graduation.spendiary.controller;

import graduation.spendiary.domain.user.User;
import graduation.spendiary.domain.user.UserService;
import graduation.spendiary.security.google.GoogleOAuthHelper;
import graduation.spendiary.security.google.GoogleOAuthLoginResponse;
import graduation.spendiary.security.google.GoogleUser;
import graduation.spendiary.security.jwt.Authorization;
import graduation.spendiary.security.jwt.JwtAuthenticationFilter;
import graduation.spendiary.security.jwt.JwtProvider;
import graduation.spendiary.security.jwt.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.net.URI;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private GoogleOAuthHelper googleOAuthHelper;

    private final String GOOGLE_APP_AUTH_URL = "paiary-app://authenticate";

    /// region Login based Application
    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Token> signUp(@RequestBody User user) {
        boolean isMember = userService.signUpRaw(user);

        if(isMember) {
            return ResponseEntity.ok().body(jwtProvider.getToken(user.getId()));
        }

        return ResponseEntity.badRequest().build();
    }

    /// endregion

    /// region Login based Google API
    @GetMapping(value = "/google/uri", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getGoogleAuthUri() {
        try{
            HttpHeaders redirectHeader = new HttpHeaders();
            redirectHeader.setLocation(new URI(googleOAuthHelper.getAuthUri()));
            return ResponseEntity.status(HttpStatus.SEE_OTHER).headers(redirectHeader).build();
        }catch (Exception e){
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping(value = "/google", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity processRedirectResult(@RequestParam(value = "code") String code) {
        try{
            GoogleOAuthLoginResponse loginResponse =  googleOAuthHelper.requestOAuthLogin(code);
            GoogleUser googleUser = googleOAuthHelper.requestProfile(loginResponse.getAccessToken(), loginResponse.getIdToken());

            HttpHeaders redirectHeader = new HttpHeaders();
            if(userService.isExistNotGoogle(googleUser.getEmail()))
                redirectHeader.setLocation(new URI(String.format("%s?error=true&code=%d",
                        GOOGLE_APP_AUTH_URL,
                        0 // already user exist
                )));
            else{
                Token token = jwtProvider.getToken(googleUser.getEmail());
                boolean isNew = userService.signUpUsingGoogle(googleUser);
                redirectHeader.setLocation(new URI(String.format("%s?error=false&access=%s&refresh=%s&fresh=%s",
                        GOOGLE_APP_AUTH_URL,
                        token.getAccess(),
                        token.getRefresh(),
                        isNew
                )));
            }
            return ResponseEntity.status(HttpStatus.SEE_OTHER).headers(redirectHeader).build();
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }



    /// endregion

    @PostMapping(value = "/authenticate", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Token> authenticate(@RequestBody Authorization authorization) {

        User user = userService.authorize(authorization.getId(), authorization.getPassword());

        if(user != null) {
            return ResponseEntity.ok().body(jwtProvider.getToken(user.getId()));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping(value = "/revoke", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Token> revoke(@RequestHeader(value = JwtAuthenticationFilter.REFRESH_HEADER_KEY) String refreshToken){
        try{
            if(StringUtils.hasText(refreshToken) && refreshToken.startsWith("Bearer ")){
                String token = refreshToken.substring(7);
                if(jwtProvider.validateRefreshToken(token)){
                    return ResponseEntity.ok(jwtProvider.getToken(
                            (String) jwtProvider.getRefreshAuthentication(token).getPrincipal()
                    ));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().build();
    }

}
