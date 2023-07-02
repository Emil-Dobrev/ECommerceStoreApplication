package emildobrev.ecommerce.store.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {


    Optional<User> findById(String id);
    Optional<User> findByEmail(String email);
    @Query("SELECT u FROM User u WHERE DAY(u.birthdate) = day AND MONTH(u.birthdate) = month")
    Optional<List<User>> findByBirthdateDayAndMonth(@Param("day") int day, @Param("month") int month);
}
