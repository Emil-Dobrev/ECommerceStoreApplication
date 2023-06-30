package emildobrev.Ecommerce.Store.product;

import emildobrev.Ecommerce.Store.enums.Category;
import emildobrev.Ecommerce.Store.exception.NoSuchElementException;
import emildobrev.Ecommerce.Store.exception.NotFoundException;
import emildobrev.Ecommerce.Store.exception.ProductCreationException;
import emildobrev.Ecommerce.Store.exception.UserAlreadyVotedException;
import emildobrev.Ecommerce.Store.product.dto.CartResponse;
import emildobrev.Ecommerce.Store.product.dto.ProductCartDTO;
import emildobrev.Ecommerce.Store.product.dto.ProductDTO;
import emildobrev.Ecommerce.Store.user.User;
import emildobrev.Ecommerce.Store.user.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

import static emildobrev.Ecommerce.Store.constants.Constants.USER_NOT_FOUND;

@Service
@AllArgsConstructor
@Slf4j
public class ProductServiceImp implements  ProductService{
    public static final String PRODUCT_NOT_FOUND_WITH_ID = "Product not found with id:";
    private ProductRepository productRepository;
    private ModelMapper modelMapper;
    private UserRepository userRepository;

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
        throw new NotFoundException(PRODUCT_NOT_FOUND_WITH_ID + id);
    }

    public Product createProduct(Product product) {
        try {
            return productRepository.save(product);
        } catch (DataAccessException e) {
            throw new ProductCreationException("Failed to create product", e);
        }
    }

    public ProductDTO updateProduct(ProductDTO productDTO) {
        Optional<Product> existingProduct = productRepository.findById(productDTO.getId());
        if (existingProduct.isPresent()) {
            merge(productDTO, existingProduct.get());
            Product updatedProduct = productRepository.save(existingProduct.get());
            return modelMapper.map(updatedProduct, ProductDTO.class);
        }
        throw new NotFoundException(PRODUCT_NOT_FOUND_WITH_ID + productDTO.getId());
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


    public void voteProduct(String id, Double rating, String email) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(PRODUCT_NOT_FOUND_WITH_ID + id));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        if (product.getVotedUsers() != null && product.getVotedUsers().containsKey(email)) {
            throw new UserAlreadyVotedException("User already voted for product with id:" + id);
        }
        product.addVote(user.getId(), rating);
        product.setRating(calculateAverageRating(product.getVotedUsers()));
        productRepository.save(product);
    }

    public Comment addCommentToProduct(Comment comment, String name) {
        Product product = productRepository.findById(comment.getProductId())
                .orElseThrow(() -> new NotFoundException(PRODUCT_NOT_FOUND_WITH_ID + comment.productId));
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

    public CartResponse addProductToCart(String productId, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(PRODUCT_NOT_FOUND_WITH_ID + productId));

        HashSet<ProductCartDTO> userCart = user.getCart();
        if (userCart == null) {
            userCart = new HashSet<>();
            user.setCart(userCart);
        }
        userCart.add(modelMapper.map(product, ProductCartDTO.class));
        userRepository.save(user);
        return CartResponse.builder()
                .cart(userCart)
                .message("Successfully added product")
                .build();
    }

    public CartResponse removeProductFromCart(String id, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(PRODUCT_NOT_FOUND_WITH_ID + id));

        HashSet<ProductCartDTO> userCart = user.getCart();
        userCart.remove(modelMapper.map(product, ProductCartDTO.class));
        userRepository.save(user);
        return CartResponse.builder()
                .cart(userCart)
                .message("Successfully removed")
                .build();
    }

    private double calculateAverageRating(HashMap<String, Double> votedUsers) {
        return votedUsers.values()
                .stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }

    private static <T> void merge(T source, T target) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.map(source, target);
    }

    private ProductDTO convertToDTO(Product product) {
        return modelMapper.map(product, ProductDTO.class);
    }
}
