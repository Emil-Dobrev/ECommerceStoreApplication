package emildobrev.ecommerce.store.product;

import emildobrev.ecommerce.store.enums.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    Page<Product> findAll(Pageable pageable);
    Optional<Product> findById(String id);
    Optional< List<Product>> findAllByCategory(Category category);
      @Query("{ 'name' : { '$regex' : ?0, '$options' : 'i' } }")
    Optional<List<Product>> getAllByNameMatchesRegex(String regexp);
}
