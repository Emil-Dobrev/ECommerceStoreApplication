package EmilDobrev.Ecommerce.Store.product;

import EmilDobrev.Ecommerce.Store.enums.Category;
import EmilDobrev.Ecommerce.Store.exception.NoSuchElementException;
import EmilDobrev.Ecommerce.Store.exception.NotFoundException;
import EmilDobrev.Ecommerce.Store.exception.UserAlreadyVotedException;
import EmilDobrev.Ecommerce.Store.product.dto.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.val;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductService {
    private ProductRepository productRepository;
    private ModelMapper modelMapper;
    private static final Logger logger = LogManager.getLogger(ProductService.class);


    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(this::convertToDTO)
                .toList();
    }

    public ProductDTO getProductById(String id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            return modelMapper.map(product, ProductDTO.class);
        }
        throw new NotFoundException("Product not found with ID:" + id);
    }

    public Product createProduct(Product product) {
        try {
            return productRepository.save(product);
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to create product", e);
        }
    }

    public ProductDTO updateProduct(ProductDTO productDTO) {
        Optional<Product> existingProduct = productRepository.findById(productDTO.getId());
        if (existingProduct.isPresent()) {
            merge(productDTO, existingProduct.get());
            Product updatedProduct = productRepository.save(existingProduct.get());
            return modelMapper.map(updatedProduct, ProductDTO.class);
        }
        throw new NotFoundException("Product not found with ID:" + productDTO.getId());
    }

    public void deleteProduct(String id) {
        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found product with id:" + id));
        productRepository.delete(existingProduct);
    }

    public List<ProductDTO> getAllProductsByCategory(Category category) {
        var products = productRepository.findAllByCategory(category).orElseThrow(() -> new NoSuchElementException("No products for category:" + category));

        return products.stream()
                .map(this::convertToDTO)
                .toList();
    }

    public static <T> void merge(T source, T target) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.map(source, target);
    }

    public void voteProduct(String id, Double rating, String email) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found with id:" + id));

        if (product.getVotedUsers() != null && product.getVotedUsers().containsKey(email)) {
            throw new UserAlreadyVotedException("User already voted for product with id:" + id);
        }
        product.addVote(email, rating);
        product.setRating(calculateAverageRating(product.getVotedUsers()));
        productRepository.save(product);
    }

    public Comment addCommentToProduct(Comment comment) {
        Product product = productRepository.findById(comment.getProductId())
                .orElseThrow(()-> new NotFoundException("Product not found with id:" + comment.productId));

        product.addComment(comment);
        productRepository.save(product);
        return comment;
    }

    public List<ProductDTO> getAllByNameRegex(String regex) {
        List<Product> products = productRepository.getAllByNameMatchesRegex(regex)
                .orElseThrow(()-> new NotFoundException("No items found"));
        return products.stream()
                .map(this::convertToDTO)
                .toList();
    }

    private double calculateAverageRating(HashMap<String, Double> votedUsers) {
        return votedUsers.values()
                .stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }

    private ProductDTO convertToDTO(Product product) {
        return modelMapper.map(product, ProductDTO.class);
    }

}
