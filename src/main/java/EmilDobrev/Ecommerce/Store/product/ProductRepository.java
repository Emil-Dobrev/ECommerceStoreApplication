package EmilDobrev.Ecommerce.Store.product;

import EmilDobrev.Ecommerce.Store.enums.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    List<Product> findAll();
    Optional<Product> findById(String id);
    Optional< List<Product>> findAllByCategory(Category category);
      @Query("{ 'name' : { '$regex' : ?0, '$options' : 'i' } }")
    Optional<List<Product>> getAllByNameMatchesRegex(String regexp);
}
