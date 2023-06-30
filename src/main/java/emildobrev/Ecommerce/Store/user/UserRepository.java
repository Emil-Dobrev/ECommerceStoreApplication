package emildobrev.Ecommerce.Store.user;

import emildobrev.Ecommerce.Store.product.dto.ProductDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);
    @Query("UPDATE User u SET u.cart = ?1 WHERE u.id= ?2")
    void updateUserCart(List<ProductDTO> cart , String id);
}
