package emildobrev.ecommerce.store.integration.product;


import emildobrev.ecommerce.store.enums.Category;
import emildobrev.ecommerce.store.product.Product;
import emildobrev.ecommerce.store.product.ProductRepository;
import emildobrev.ecommerce.store.product.ProductService;
import emildobrev.ecommerce.store.product.ProductServiceImp;
import emildobrev.ecommerce.store.product.dto.ProductCartDTO;
import emildobrev.ecommerce.store.product.dto.ProductDTO;
import emildobrev.ecommerce.store.product.dto.WishListResponse;
import emildobrev.ecommerce.store.user.Role;
import emildobrev.ecommerce.store.user.User;
import emildobrev.ecommerce.store.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;


import java.math.BigDecimal;
import java.util.*;

import static com.mongodb.assertions.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


class ProductControllerIntegrationTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ModelMapper modelMapper;

    @Captor
    private ArgumentCaptor<Product> productCaptor;

    @InjectMocks
    private ProductServiceImp productService;
    private User user;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        user = User.builder()
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

    @Test
    void testUpdateProduct() {
        // Mock data
        ProductDTO productDTO = ProductDTO.builder()
                .id("1")
                .price(BigDecimal.valueOf(2.55))
                .description("")
                .name("Product")
                .category(Category.BOOKS)
                .build();


        Product existingProduct = new Product();
        existingProduct.setId("1");
        existingProduct.setPrice(BigDecimal.valueOf(5));
        existingProduct.setDescription("");


        Product updatedProduct = new Product();
        updatedProduct.setId("1");
        updatedProduct.setPrice(BigDecimal.valueOf(2.55));
        updatedProduct.setDescription("");


        when(productRepository.findById(productDTO.getId())).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(existingProduct)).thenReturn(updatedProduct);


        when(modelMapper.map(updatedProduct, ProductDTO.class)).thenReturn(productDTO);


        ProductDTO result = productService.updateProduct(productDTO);


        verify(productRepository).findById(productDTO.getId());
        verify(productRepository).save(existingProduct);


        verify(modelMapper).map(updatedProduct, ProductDTO.class);

        // Assertions
        Assertions.assertEquals(productDTO.getId(), result.getId());
        Assertions.assertEquals(productDTO.getPrice(), result.getPrice());
        // Add more assertions for other properties if needed
    }

    @Test
    void voteProduct_WithValidData_ShouldSaveProductWithUpdatedRating() {
        // Arrange
        String productId = "123";
        String userEmail = "test@example.com";
        Double userRating = 4.5;

        Product product = new Product();
        HashMap<String, Double> vote = new HashMap();
        vote.put("test", 6.5);
        product.setVotedUsers(vote);
        user.setEmail(userEmail);

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));


        // Act
        productService.voteProduct(productId, userRating, userEmail);

        verify(productRepository, times(1)).save(productCaptor.capture());

        // Assert
        Product result = productCaptor.getValue();

        Assertions.assertEquals(2, result.getVotedUsers().size());
        Assertions.assertTrue(result.getVotedUsers().containsKey(user.getId()));
        Assertions.assertEquals(userRating, result.getVotedUsers().get(user.getId()));
        Assertions.assertEquals(result.getRating(), 5.5);

    }
}
