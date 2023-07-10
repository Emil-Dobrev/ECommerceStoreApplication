package emildobrev.ecommerce.store.product;

import emildobrev.ecommerce.store.enums.Category;
import emildobrev.ecommerce.store.exception.NoSuchElementException;
import emildobrev.ecommerce.store.exception.NotFoundException;
import emildobrev.ecommerce.store.exception.ProductCreationException;
import emildobrev.ecommerce.store.exception.UserAlreadyVotedException;
import emildobrev.ecommerce.store.product.dto.CartResponse;
import emildobrev.ecommerce.store.product.dto.ProductCartDTO;
import emildobrev.ecommerce.store.product.dto.ProductDTO;
import emildobrev.ecommerce.store.product.dto.WishListResponse;
import emildobrev.ecommerce.store.user.User;
import emildobrev.ecommerce.store.user.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

import static emildobrev.ecommerce.store.constants.Constants.USER_NOT_FOUND;

@Service
@AllArgsConstructor
@Slf4j
public class ProductServiceImp implements ProductService {
    public static final String PRODUCT_NOT_FOUND_WITH_ID = "Product not found with id:";
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final MongoTemplate mongoTemplate;


    public Page<ProductDTO> getAllProducts(Pageable pageable,
                                           Double minRating) {
        Query query = new Query().with(pageable);

        if(minRating != null) {
            query.addCriteria(Criteria.where("rating").lte(minRating));
            Sort sort = Sort.by(Sort.Order.desc("rating"));
            query.with(sort);
        }

       return PageableExecutionUtils.getPage(
                mongoTemplate.find(query, ProductDTO.class),
                pageable,
                () -> mongoTemplate.count(query.skip(0).limit(0), ProductDTO.class));
    }

    @Override
    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        return null;
    }

    public ProductDTO getProductById(String id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            return modelMapper.map(product, ProductDTO.class);
        }
        throw new NotFoundException(PRODUCT_NOT_FOUND_WITH_ID + id);
    }

    public Product createProduct(ProductDTO productDTO) {
        try {
            Product product = modelMapper.map(productDTO, Product.class);
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

        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        if (product.getVotedUsers() != null && product.getVotedUsers().containsKey(user.getId())) {
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

    public CartResponse addProductToCart(String productId, int quantity, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(PRODUCT_NOT_FOUND_WITH_ID + productId));

        HashSet<ProductCartDTO> userCart = user.getCart();
        if (userCart == null) {
            userCart = new HashSet<>();
            user.setCart(userCart);
        }
        ProductCartDTO productCartDTO = modelMapper.map(product, ProductCartDTO.class);
        productCartDTO.setOrderQuantity(quantity);
        productCartDTO.setPrice(productCartDTO.getPrice().multiply(BigDecimal.valueOf(quantity)));

        userCart.add(productCartDTO);
        userRepository.save(user);
        return CartResponse.builder()
                .cart(userCart)
                .message("Successfully added product")
                .build();
    }

    public CartResponse removeProductFromCart(String id, String email) {
        var user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));

        HashSet<ProductCartDTO> userCart = user.getCart();
        boolean removed = userCart.removeIf(e -> Objects.equals(e.getId(), id)); // Remove the object with the same ID


        if (removed) {
            userRepository.save(modelMapper.map(user, User.class));
        }
        return CartResponse.builder()
                .cart(userCart)
                .message(removed ? "Successfully removed" : "No matching product found")
                .build();
    }

    @Override
    public WishListResponse addProductToWishlist(String id, String email) {
        var user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(PRODUCT_NOT_FOUND_WITH_ID + id));
        var cartDto = modelMapper.map(product, ProductCartDTO.class);
        var wishList = user.getWishList();

        if (wishList == null) {
            wishList = new HashSet<>();
        }

        wishList.add(cartDto);
        user.setWishList(wishList);
        userRepository.save(user);

        return WishListResponse.builder()
                .productName(product.getName())
                .message("Successfully added product to wishlist")
                .build();
    }

    @Override
    public WishListResponse removeProductFromWishlist(String id, String email) {
        var user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(PRODUCT_NOT_FOUND_WITH_ID + id));

        HashSet<ProductCartDTO> userWishList = user.getWishList();
        boolean removed = userWishList.removeIf(e -> Objects.equals(e.getId(), id));

        if(removed) userRepository.save(user);

        return WishListResponse.builder()
                .productName(product.getName())
                .message(removed ? "Successfully removed" : "No matching product found")
                .build();
    }

    @Override
    public List<ProductDTO> compareProducts(List<String> productIds) {
        return productIds.stream()
                .map(productRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
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

    private static <T> void merge(T source, T target) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.map(source, target);
    }

    private ProductDTO convertToDTO(Product product) {
        return modelMapper.map(product, ProductDTO.class);
    }
}
