package EmilDobrev.Ecommerce.Store.product;

import EmilDobrev.Ecommerce.Store.enums.Category;
import EmilDobrev.Ecommerce.Store.product.dto.ProductDTO;
import EmilDobrev.Ecommerce.Store.product.dto.RatingDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {

    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable String id) {
        ProductDTO product = productService.getProductById(id);
        if (product != null) {
            return ResponseEntity.status(HttpStatus.OK).body(product);
        }
        return ResponseEntity.notFound().build();
    }

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
    public ResponseEntity<HttpStatus> voteProduct( @RequestBody RatingDTO ratingDTO, String email) {
        email = "Test";
        productService.voteProduct(ratingDTO.getId(), ratingDTO.getRating(), email);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/category")
    public ResponseEntity<List<ProductDTO>> getAllProductsByCategory(@RequestBody ProductDTO productDTO){
        Category category = productDTO.getCategory();
        List<ProductDTO> products = productService.getAllProductsByCategory(category);
        return ResponseEntity.ok().body(products);
    }
    //TODO fulltext search and Category product selection
}
