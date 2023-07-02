package emildobrev.ecommerce.store.product;

import emildobrev.ecommerce.store.enums.Category;
import emildobrev.ecommerce.store.product.dto.CartResponse;
import emildobrev.ecommerce.store.product.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    Page<ProductDTO> getAllProducts(Pageable pageable);
    ProductDTO getProductById(String id);
    Product createProduct(Product product);
    ProductDTO updateProduct(ProductDTO productDTO);
    void deleteProduct(String id);
    List<ProductDTO> getAllProductsByCategory(Category category);
    void voteProduct(String id, Double rating, String email);
    Comment addCommentToProduct(Comment comment, String name);
    List<ProductDTO> getAllByNameRegex(String regex);
    CartResponse addProductToCart(String productId,int quantity, String email);
    CartResponse removeProductFromCart(String id, String email);
}