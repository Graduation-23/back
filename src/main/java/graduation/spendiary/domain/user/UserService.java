package graduation.spendiary.domain.user;

import graduation.spendiary.domain.cdn.CloudinaryService;
import graduation.spendiary.exception.NoSuchContentException;
import graduation.spendiary.security.google.GoogleUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService {
    @Autowired
    private UserRepository repo;
    @Autowired
    private CloudinaryService cloudinaryService;

    public static Pattern EMAIL_PATTERN = Pattern.compile("^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\\\w+\\\\.)+\\\\w+$");

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
        if (!EMAIL_PATTERN.matcher(user.getId()).matches())
            return false;
        if (user.getId().length() < 5)
            return false;
        if (user.getNickname().length() < 3 || user.getNickname().length() > 7)
            return false;
        boolean hasSpecialChar = "!@#$%^&*()-_=+"
                .chars()
                .mapToObj(o -> (char)o)
                .anyMatch(specialChar -> user.getPassword().contains(specialChar.toString()));
        if (user.getPassword().length() < 5 || user.getPassword().length() > 15 || !hasSpecialChar)
            return false;
        if(isExist(user.getId()))
            return false;

        User member = repo.save(User.builder()
                .nickname(user.getNickname()).id(user.getId())
                .accessType("none")
                .password(user.getPassword())
                .birth((user.getBirth()))
                .created(LocalDate.now())
                .build());

        return member != null;
    }

    public boolean signUpUsingGoogle(GoogleUser googleUser) {

        User user = User.builder()
                .accessType("google")
                .id(googleUser.getEmail())
                .nickname(googleUser.getName())
                .password("")
                .created(LocalDate.now())
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

    public boolean validateUser(String userId, String password) {
        return repo.findByIdAndPw(userId, password) != null;
    }

    public boolean deleteUser(String userId, String password) {
        User user = repo.findByIdAndPw(userId, password);
        if(user == null) {
            return false;
        }
        repo.delete(user);
        return true;
    }

    public User setBirthday(String userId, LocalDate birthday)
        throws NoSuchContentException
    {
        Optional<User> userOptional = repo.findById(userId);
        if (userOptional.isEmpty())
            throw new NoSuchContentException();
        User user = userOptional.get();

        user.setBirth(birthday);
        return repo.save(user);
    }

    public User setProfilePic(String userId, MultipartFile profilePic)
        throws NoSuchContentException, IOException
    {
        Optional<User> userOptional = repo.findById(userId);
        if (userOptional.isEmpty())
            throw new NoSuchContentException();
        User user = userOptional.get();

        String url = cloudinaryService.upload(profilePic);
        user.setProfilePicUrl(url);
        return repo.save(user);
    }
}