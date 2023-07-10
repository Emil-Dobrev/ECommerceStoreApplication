package emildobrev.ecommerce.store.product;

import emildobrev.ecommerce.store.enums.Category;
import emildobrev.ecommerce.store.product.dto.CartResponse;
import emildobrev.ecommerce.store.product.dto.ProductDTO;
import emildobrev.ecommerce.store.product.dto.WishListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    Page<ProductDTO> getAllProducts(Pageable pageable, Double minRating);
    ProductDTO getProductById(String id);
    Product createProduct(ProductDTO product);
    ProductDTO updateProduct(ProductDTO productDTO);
    void deleteProduct(String id);
    List<ProductDTO> getAllProductsByCategory(Category category);
    void voteProduct(String id, Double rating, String email);
    Comment addCommentToProduct(Comment comment, String name);
    List<ProductDTO> getAllByNameRegex(String regex);
    CartResponse addProductToCart(String productId,int quantity, String email);
    CartResponse removeProductFromCart(String id, String email);
    WishListResponse addProductToWishlist(String id, String email);
    WishListResponse removeProductFromWishlist(String id, String email);
    List<ProductDTO> compareProducts(List<String> productIds);
}
