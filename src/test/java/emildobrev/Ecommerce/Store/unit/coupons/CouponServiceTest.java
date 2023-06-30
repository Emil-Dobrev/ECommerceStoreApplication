package emildobrev.Ecommerce.Store.unit.coupons;

import emildobrev.Ecommerce.Store.coupons.Coupon;
import emildobrev.Ecommerce.Store.coupons.CouponRepository;
import emildobrev.Ecommerce.Store.coupons.CouponService;
import emildobrev.Ecommerce.Store.enums.Category;
import emildobrev.Ecommerce.Store.product.dto.ProductDTO;
import emildobrev.Ecommerce.Store.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CouponServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private CouponRepository couponRepository;

    @InjectMocks
    private CouponService couponService;

    @BeforeEach
    public void setUp() {
        couponService = new CouponService(userRepository, couponRepository);
    }


    @Test
    void testReducePrice() {
        // Create a sample coupon with a discount of 20%
        Coupon coupon = Coupon.builder()
                .id("1")
                .discount(20)
                .validFrom(Instant.now())
                .validTo(Instant.now().plusSeconds(15000L))
                .build();

        // Create a sample list of products with their original prices
        List<ProductDTO> cart = new ArrayList<>();
        cart.add(new ProductDTO("Product 1", "", "", new BigDecimal("100.00"), Category.ELECTRONIC, List.of()));
        cart.add(new ProductDTO("Product 2", "", "", new BigDecimal("50.00"), Category.BOOKS, List.of()));

        // Call the reducePrice method
        List<ProductDTO> reducedCart = couponService.reducePrice(cart, coupon);

        for (ProductDTO product : reducedCart) {
            System.out.println("Product: " + product.getName() + ", Price: " + product.getPrice());
        }

        // Check if the prices of products in the reduced cart are updated correctly
        assertEquals(new BigDecimal("80.00"), reducedCart.get(0).getPrice());
        assertEquals(new BigDecimal("40.00"), reducedCart.get(1).getPrice());
    }

}
