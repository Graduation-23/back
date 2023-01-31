package graduation.spendiary.controller;

import graduation.spendiary.domain.user.User;
import graduation.spendiary.domain.user.UserService;
import graduation.spendiary.security.google.GoogleOAuthHelper;
import graduation.spendiary.security.google.GoogleOAuthLoginResponse;
import graduation.spendiary.security.google.GoogleUser;
import graduation.spendiary.security.jwt.Authorization;
import graduation.spendiary.security.jwt.JwtProvider;
import graduation.spendiary.security.jwt.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    @PostMapping("/signup")
    public String signUp(@RequestBody User user) {
        boolean success = userService.signUpRaw(user);
        return success ? "성공" : "이미 있는 사용자";
    }

    /// endregion

    /// region Login based Google API
    @GetMapping("/google/uri")
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

    @GetMapping("/google")
    public ResponseEntity processRedirectResult(@RequestParam(value = "code") String code) {
        try{
            GoogleOAuthLoginResponse loginResponse =  googleOAuthHelper.requestOAuthLogin(code);
            GoogleUser googleUser = googleOAuthHelper.requestProfile(loginResponse.getAccessToken(), loginResponse.getIdToken());

            HttpHeaders redirectHeader = new HttpHeaders();
            // 가입되지 않는 사용자의 경우 서비스에 가입
            if(userService.signUpUsingGoogle(googleUser)){
                Token token = jwtProvider.getToken(googleUser.getEmail());
                redirectHeader.setLocation(new URI(String.format("%s?error=false&access=%s&refresh=%s",
                        GOOGLE_APP_AUTH_URL,
                        token.getAccess(),
                        token.getRefresh()
                )));
            }else {
                redirectHeader.setLocation(new URI(String.format("%s?error=true", GOOGLE_APP_AUTH_URL)));
            }
            return ResponseEntity.status(HttpStatus.SEE_OTHER).headers(redirectHeader).build();
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//       return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }



    /// endregion

    @PostMapping("/authenticate")
    public Token authenticate(@RequestBody Authorization authorization) {

        User user = userService.authorize(authorization.getId(), authorization.getPassword());

        if(user != null) {
            return jwtProvider.getToken(user.getId());
        }
        return null;
    }

}
