package EmilDobrev.Ecommerce.Store.product;

import EmilDobrev.Ecommerce.Store.enums.Category;
import EmilDobrev.Ecommerce.Store.exception.NoSuchElementException;
import EmilDobrev.Ecommerce.Store.exception.NotFoundException;
import EmilDobrev.Ecommerce.Store.exception.UserAlreadyVotedException;
import EmilDobrev.Ecommerce.Store.product.dto.ProductDTO;
import EmilDobrev.Ecommerce.Store.user.User;
import EmilDobrev.Ecommerce.Store.user.UserRepository;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductService {
    private ProductRepository productRepository;
    private ModelMapper modelMapper;
    private UserRepository userRepository;
    private static final Logger logger = LogManager.getLogger(ProductService.class);


    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        return PageableExecutionUtils.getPage(
                products.stream()
                        .map(this::convertToDTO)
                        .toList(),
                pageable,
                products::getTotalElements
        );

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

    public Comment addCommentToProduct(Comment comment, String name) {
        Product product = productRepository.findById(comment.getProductId())
                .orElseThrow(() -> new NotFoundException("Product not found with id:" + comment.productId));
        comment.setCreatedBy(name);
        product.addComment(comment);
        productRepository.save(product);
        return comment;
    }

    public List<ProductDTO> getAllByNameRegex(String regex) {
        List<Product> products = productRepository.getAllByNameMatchesRegex(regex)
                .orElseThrow(() -> new NotFoundException("No items found"));
        return products.stream()
                .map(this::convertToDTO)
                .toList();
    }

    public void addProductToCart(String productId, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found with id:" + productId));

        List<ProductDTO> userCart = user.getCart();
        if (userCart == null) {
            userCart = new ArrayList<>();
            user.setCart(userCart);
        }
        userCart.add(convertToDTO(product));
        userRepository.save(user);
    }

    public void removeProductFromCart(String id, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found with id:" + id));

        List<ProductDTO> userCart = user.getCart();
        userCart.remove(convertToDTO(product));
        userRepository.save(user);
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
