package emildobrev.ecommerce.store.unit.product;

import emildobrev.ecommerce.store.enums.Category;
import emildobrev.ecommerce.store.product.Product;
import emildobrev.ecommerce.store.product.ProductRepository;
import emildobrev.ecommerce.store.product.ProductServiceImp;
import emildobrev.ecommerce.store.product.dto.ProductDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ProductServiceImp productService;
    @Captor
    private ArgumentCaptor<Product> productCaptor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUpdateProduct() {
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
    public void voteProduct_WithValidData_ShouldSaveProductWithUpdatedRating() {
        // Arrange
        String productId = "123";
        String userEmail = "test@example.com";
        Double userRating = 4.5;

        Product product = new Product();
        HashMap<String, Double> hashMap = new HashMap();
        hashMap.put("test", 6.5);
        product.setVotedUsers(hashMap);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Act
        productService.voteProduct(productId, userRating, userEmail);

        verify(productRepository, times(1)).save(productCaptor.capture());

        // Assert
        Product result = productCaptor.getValue();

        Assertions.assertEquals(2, result.getVotedUsers().size());
        Assertions.assertTrue(result.getVotedUsers().containsKey(userEmail));
        Assertions.assertEquals(userRating, result.getVotedUsers().get(userEmail));
        Assertions.assertEquals(result.getRating(),5.5);

    }

}
