package graduation.spendiary.controller;

import graduation.spendiary.domain.user.User;
import graduation.spendiary.domain.user.UserService;
import graduation.spendiary.exception.NoSuchContentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public User getInformation(@AuthenticationPrincipal String userId) {
        return userService.getUser(userId);
    }

    @DeleteMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Message deleteInformation(@AuthenticationPrincipal String userId, @RequestParam("password") String password) {

        boolean success = userService.deleteUser(userId, password);

        return new Message(
                success ? "탈퇴 완료" : "탈퇴 실패; 패스워드를 확인하세요",
                success
        );
    }

    @PutMapping (value = "/birth", consumes = MediaType.APPLICATION_JSON_VALUE)
    public User putBirthday(
            @AuthenticationPrincipal String userId,
            @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate birthday
    ) throws NoSuchContentException
    {
        userService.setBirthday(userId, birthday);
        return userService.getUser(userId);
    }

    @PutMapping(value = "/profile-pic", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public User putProfilePic(
            @AuthenticationPrincipal String userId,
            @RequestParam MultipartFile profilePic
    ) throws NoSuchContentException, IOException
    {
        return userService.setProfilePic(userId, profilePic);
    }
}
