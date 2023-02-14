package graduation.spendiary.controller;

import graduation.spendiary.domain.user.User;
import graduation.spendiary.domain.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

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
    public Message deleteInformation(@AuthenticationPrincipal String userId, String password) {
        if(!(userService.deleteUser(userId, password))){
            return new Message("탈퇴 실패; 패스워드를 확인하세요", false);
        }
        userService.deleteUser(userId, password);
        return new Message("탈퇴 완료", true);
    }

}
