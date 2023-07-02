package emildobrev.ecommerce.store.coupons;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface  CouponRepository extends MongoRepository<Coupon, String> {
    Optional<List<Coupon>> findByValidToBefore(Instant now);
}
