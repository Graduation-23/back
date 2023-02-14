package graduation.spendiary.domain.diary;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DiaryRepository extends MongoRepository<Diary, Long> {
    List<Diary> findByUser(String userId);

    @Query(value = "{'user_id': ?0, 'diary_date': ?1}")
    List<Diary> findByUserAndDate(String userId, LocalDate date);

    @Query(value = "{'user_id': ?0, 'diary_create': {$gte: ?1, $lte: ?2}}")
    List<Diary> findByUserAndCreatedBetween(String userId, LocalDate start, LocalDate end);
}
