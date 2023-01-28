package graduation.spendiary.domain.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface UserRepository extends MongoRepository<User, String> {

    @Query("{_id:'?0', user_password:'?1'}")
    User findByIdAndPw(String id, String pw);
}
