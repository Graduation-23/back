package graduation.spendiary.controller;

import graduation.spendiary.domain.user.User;
import graduation.spendiary.domain.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public User getInformation(@AuthenticationPrincipal String userId) {
        return userService.getUser(userId);
    }

    @DeleteMapping
    public Message deleteInformation(@AuthenticationPrincipal String userId, @RequestParam("password") String password) {

        boolean success = userService.deleteUser(userId, password);

        return new Message(
                success ? "탈퇴 완료" : "탈퇴 실패; 패스워드를 확인하세요",
                success
        );
    }

    @PutMapping ("/birth")
    public User birthdayInformation(@AuthenticationPrincipal String userId, @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate birthday) {
        userService.birthday(userId, birthday);
        return userService.getUser(userId);
    }
}
