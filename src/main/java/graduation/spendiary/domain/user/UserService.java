package graduation.spendiary.domain.user;

import graduation.spendiary.security.google.GoogleUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository repo;

    public List<User> getAll() {
        return repo.findAll();
    }

    public boolean isExist(String id) {
        return repo.existsById(id);
    }

    public boolean isExistNotGoogle(String id) {
        return repo.findByNotGoogleId(id) != null;
    }

    public boolean isValid(String id, String password) {
        User user = repo.findByIdAndPw(id, password);

        return user != null;
    }

    public User authorize(String id, String password) {
        return repo.findByIdAndPw(id, password);
    }

    public boolean signUpRaw(User user) {

        if(isExist(user.getId())) return false;

        User member = repo.save(User.builder()
                .nickname(user.getNickname()).id(user.getId())
                .accessType("none")
                .password(user.getPassword())
                .birth((user.getBirth()))
                .build());

        return member != null;
    }

    public boolean signUpUsingGoogle(GoogleUser googleUser) {

        User user = User.builder()
                .accessType("google")
                .id(googleUser.getEmail())
                .nickname(googleUser.getName())
                .password("")
                .build();

        if(repo.findByGoogleId(user.getId()) == null){
            repo.save(user);
            return true;
        }
        return false;
    }

    public User getUser(String userId) {
        return repo.findById(userId).get();
    }

    public boolean deleteUser(String userId, String password) {
        User user = repo.findByIdAndPw(userId, password);
        if(user == null) {
            return false;
        }
        repo.delete(user);
        return true;
    }

    public User birthday(String  userId, LocalDate birthday) {
        User updateUser = repo.findById(userId).orElse(null);
        updateUser.setBirth(birthday);
        return repo.save(updateUser);
    }
}