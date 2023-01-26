package graduation.spendiary;

import graduation.spendiary.domain.diary.Diary;
import graduation.spendiary.domain.user.User;
import graduation.spendiary.domain.diary.DiaryService;
import graduation.spendiary.domain.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class SpendiaryApplicationTests {
    @Autowired
    private UserService userService;
    @Autowired
    private DiaryService diaryService;

    @Test
    void userServiceTest() {
        List<User> users = userService.getAll();
        System.out.println(users);
    }

    @Test
    void diaryServiceTest() {
        List<Diary> diaries = diaryService.getAll();
        System.out.println(diaries);
    }

}