package emildobrev.ecommerce.store.product;

import emildobrev.ecommerce.store.product.dto.*;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;


@RestController
@RequestMapping("/api/v1/products")
@AllArgsConstructor
public class ProductController {

    private ProductServiceImp productService;

    @GetMapping
    @Cacheable(value = "products", key = "#page + '-' + #size + '-' + #minRating", condition = "#minRating == null")
    @CacheEvict(value = "products", key = "#page + '-' + #size + '-' + #minRating", condition = "#minRating != null")
    public ResponseEntity<Page<ProductDTO>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size,
            @RequestParam(required = false)  Double minRating
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok().body(productService.getAllProducts(pageable, minRating));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable String id) {
        ProductDTO product = productService.getProductById(id);
        if (product != null) {
            return ResponseEntity.status(HttpStatus.OK).body(product);
        }
        return ResponseEntity.notFound().build();
    }


    @GetMapping("/compare")
    public ResponseEntity<List<ProductDTO>> compareProducts(@RequestBody @NonNull List<String> productIds) {
        return ResponseEntity.ok().body(productService.compareProducts(productIds));
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody ProductDTO product) {
        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.ok(createdProduct);
    }

    @PatchMapping()
    public ResponseEntity<ProductDTO> updateProduct(@RequestBody ProductDTO productDTO) {
        ProductDTO updatedProduct = productService.updateProduct(productDTO);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/vote")
    public ResponseEntity<HttpStatus> voteProduct(@RequestBody RatingDTO ratingDTO, Authentication authentication) {
        productService.voteProduct(ratingDTO.getId(), ratingDTO.getRating(), authentication.getName());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/category")
    public ResponseEntity<List<ProductDTO>> getAllProductsByCategory(@RequestBody SearchByCategoryDto category) {
        List<ProductDTO> products = productService.getAllProductsByCategory(category.getCategory());
        return ResponseEntity.ok().body(products);
    }

    @PostMapping("/comment")
    public ResponseEntity<Comment> addComment(@RequestBody Comment comment, Authentication authentication) {
        return ResponseEntity.ok().body(productService.addCommentToProduct(comment, authentication.getName()));
    }

    @GetMapping("/search/{regex}")
    public ResponseEntity<List<ProductDTO>> searchProductsByName(@PathVariable String regex) {
        return ResponseEntity.ok(productService.getAllByNameRegex(regex));
    }

    @PostMapping("/cart/add/{id}")
    public ResponseEntity<CartResponse> addProductToCart(@PathVariable String id,
                                                         @RequestParam("quantity") int quantity,
                                                         Authentication authentication) {
        return ResponseEntity.ok().body(productService.addProductToCart(id,quantity, authentication.getName()));
    }

    @PutMapping("/cart/remove/{id}")
    public ResponseEntity<CartResponse> removeProductFromCart(@PathVariable String id, Authentication authentication) {
        return ResponseEntity.ok().body(productService.removeProductFromCart(id, authentication.getName()));
    }

    @PostMapping("/wishlist/add/{id}")
    public ResponseEntity<WishListResponse> addProductToWishList(@PathVariable @NonNull String id,
                                                                 Authentication authentication) {
        return ResponseEntity.ok().body(productService.addProductToWishlist(id, authentication.getName()));
    }

    @PutMapping("/wishlist/remove/{id}")
    public ResponseEntity<WishListResponse> removeProductFromWishlist(@PathVariable @NonNull String id,
                                                                      Authentication authentication) {
        return ResponseEntity.ok().body(productService.removeProductFromWishlist(id, authentication.getName()));
    }

}
