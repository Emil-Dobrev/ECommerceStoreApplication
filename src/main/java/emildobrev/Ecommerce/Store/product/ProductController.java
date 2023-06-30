package emildobrev.Ecommerce.Store.product;

import emildobrev.Ecommerce.Store.enums.Category;
import emildobrev.Ecommerce.Store.product.dto.CartResponse;
import emildobrev.Ecommerce.Store.product.dto.ProductDTO;
import emildobrev.Ecommerce.Store.product.dto.RatingDTO;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/products")
@AllArgsConstructor
public class ProductController {

    private ProductServiceImp productService;

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDTO> productPage = productService.getAllProducts(pageable);
        List<ProductDTO> productDTOList = productPage.getContent().stream().toList();
        return ResponseEntity.ok(new PageImpl<>(productDTOList, pageable, productPage.getTotalElements()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable String id) {
        ProductDTO product = productService.getProductById(id);
        if (product != null) {
            return ResponseEntity.status(HttpStatus.OK).body(product);
        }
        return ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
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
    public ResponseEntity<List<ProductDTO>> getAllProductsByCategory(@RequestBody ProductDTO productDTO) {
        Category category = productDTO.getCategory();
        List<ProductDTO> products = productService.getAllProductsByCategory(category);
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

    @PutMapping("/cart/add/{id}")
    public ResponseEntity<CartResponse> addProductToCart(@PathVariable String id,
                                                         @RequestParam("quantity") int quantity,
                                                         Authentication authentication) {
        return ResponseEntity.ok().body(productService.addProductToCart(id,quantity, authentication.getName()));
    }

    @PutMapping("/cart/remove/{id}")
    public ResponseEntity<CartResponse> removeProductFromCart(@PathVariable String id, Authentication authentication) {
        return ResponseEntity.ok().body(  productService.removeProductFromCart(id, authentication.getName()));
    }
}
