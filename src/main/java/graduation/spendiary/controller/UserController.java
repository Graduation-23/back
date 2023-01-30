package graduation.spendiary.controller;

import graduation.spendiary.domain.user.User;
import graduation.spendiary.domain.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public String signUp(@RequestBody User user) {
        boolean success = userService.signUp(user);

        return success ? "성공" : "이미 있는 사용자";
    }

    @GetMapping("/{userId}")
    public boolean exist(@PathVariable String userId) {
        return userService.isExist(userId);
    }

}
