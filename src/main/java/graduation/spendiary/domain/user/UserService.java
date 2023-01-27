package graduation.spendiary.domain.user;

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

    public boolean isValid(String id, String password) {
        User user = repo.findByIdAndPw(id, password);

        return user != null;
    }

    public User authorize(String id, String password) {
        return repo.findByIdAndPw(id, password);
    }

    public boolean signUp(User user) {

        if(isExist(user.getId())) return false;

        user.setAccessToken("");
        user.setRefreshToken("");

        repo.save(user);

        return true;
    }

}
