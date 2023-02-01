package graduation.spendiary.domain.user;

import graduation.spendiary.security.google.GoogleUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        repo.save(user);

        return true;
    }

    public boolean signUpUsingGoogle(GoogleUser googleUser) {

        User user = new User();

        user.setAccessType("google");
        user.setId(googleUser.getEmail());
        user.setNickname(googleUser.getName());

        if(repo.findByGoogleId(user.getId()) == null){
            repo.save(user);
            return true;
        }
        return false;
    }

}
