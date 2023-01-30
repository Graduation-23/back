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
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private GoogleOAuthHelper googleOAuthHelper;

    /// region Login based Application
    @PostMapping("/signup")
    public String signUp(@RequestBody User user) {
        boolean success = userService.signUp(user);
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
    public String processRedirectResult(@RequestParam(value = "code") String code) {
        try{
            GoogleOAuthLoginResponse loginResponse =  googleOAuthHelper.requestOAuthLogin(code);
            return googleOAuthHelper.requestProfile(loginResponse.getAccessToken(), loginResponse.getIdToken()).getEmail();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
//       return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }



    /// endregion

    @PostMapping("/authenticate")
    public Token authenticate(@RequestBody Authorization authorization) {

        User user = userService.authorize(authorization.getId(), authorization.getPassword());

        if(user != null) {

            Authentication authentication = new UsernamePasswordAuthenticationToken(user.getId(), null, null);

            return new Token(
                    jwtProvider.issueAccessToken(authentication),
                    jwtProvider.issueRefreshToken(authentication)
            );
        }
        return null;
    }

}
