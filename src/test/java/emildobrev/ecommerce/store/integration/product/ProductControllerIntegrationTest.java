package emildobrev.ecommerce.store.integration.product;


import emildobrev.ecommerce.store.enums.Category;
import emildobrev.ecommerce.store.product.Product;
import emildobrev.ecommerce.store.product.ProductRepository;
import emildobrev.ecommerce.store.product.ProductService;
import emildobrev.ecommerce.store.product.ProductServiceImp;
import emildobrev.ecommerce.store.product.dto.ProductCartDTO;
import emildobrev.ecommerce.store.product.dto.WishListResponse;
import emildobrev.ecommerce.store.user.Role;
import emildobrev.ecommerce.store.user.User;
import emildobrev.ecommerce.store.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;


import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;

import static com.mongodb.assertions.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;



public class ProductControllerIntegrationTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ProductServiceImp productService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRemoveProductFromWishlist() {

        ProductCartDTO productWishlist = ProductCartDTO.builder()
                .id("product_id")
                .name("Sample Product")
                .description("Product description")
                .price(BigDecimal.valueOf(9.99))
                .category(Category.CLOTHING)
                .orderQuantity(2)
                .build();
        // Mock data
        String id = "product_id";
        String email = "user@example.com";
        Product product = new Product();
        product.setId(id);
        product.setName("Sample Product");
        User user = User.builder()
                .id("user_id")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password123")
                .cart(new HashSet<>())
                .roles(Arrays.asList(Role.USER))
                .birthdate(new Date())
                .coupons(new HashSet<>())
                .wishList(new HashSet<>())
                .build();

        var wishlist = user.getWishList();
        wishlist.add(productWishlist);

        // Mock repository methods
        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        Mockito.when(productRepository.findById(id)).thenReturn(Optional.of(product));
        Mockito.when(userRepository.save(user)).thenReturn(user);

        // Execute the method
        WishListResponse response = productService.removeProductFromWishlist(id, email);

        // Verify the results
        assertNotNull(response);
        assertEquals("Sample Product", response.getProductName());
        assertEquals("Successfully removed", response.getMessage());
        assertTrue(user.getWishList().isEmpty());

        // Verify repository method invocations
        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(email);
        Mockito.verify(productRepository, Mockito.times(1)).findById(id);
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }
}
