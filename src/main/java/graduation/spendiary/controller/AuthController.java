package graduation.spendiary.controller;

import graduation.spendiary.domain.diary.Diary;
import graduation.spendiary.domain.diary.DiaryService;
import graduation.spendiary.domain.user.User;
import graduation.spendiary.domain.user.UserService;
import graduation.spendiary.security.jwt.Authorization;
import graduation.spendiary.security.jwt.JwtAuthenticationFilter;
import graduation.spendiary.security.jwt.JwtProvider;
import graduation.spendiary.security.jwt.Token;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @PostMapping("/signup")
    public String signUp(@RequestBody User user) {
        boolean success = userService.signUp(user);
        return success ? "성공" : "이미 있는 사용자";
    }

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
