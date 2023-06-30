package emildobrev.Ecommerce.Store.unit.coupon;

import emildobrev.Ecommerce.Store.coupons.Coupon;
import emildobrev.Ecommerce.Store.coupons.CouponRepository;
import emildobrev.Ecommerce.Store.coupons.CouponServiceImp;
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
import java.math.RoundingMode;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class CouponServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private CouponRepository couponRepository;

    @InjectMocks
    private CouponServiceImp couponService;

    @BeforeEach
    public void setUp() {
        couponService = new CouponServiceImp(userRepository, couponRepository);
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
        HashSet<ProductDTO> cart = new HashSet<>();
        cart.add(new ProductDTO("Product 1", "", "", new BigDecimal("100.00"), Category.ELECTRONIC,1, List.of()));
        cart.add(new ProductDTO("Product 2", "", "", new BigDecimal("50.00"), Category.BOOKS,1, List.of()));

        // Call the reducePrice method
        HashSet<ProductDTO> reducedCart = couponService.reducePrice(cart, coupon);

        BigDecimal expectedPrice1 = new BigDecimal("80.00").setScale(2, RoundingMode.HALF_UP);
        BigDecimal expectedPrice2 = new BigDecimal("40.00").setScale(2, RoundingMode.HALF_UP);


        for (ProductDTO product : reducedCart) {
            if (product.getName().equals("Product 1")) {
                assertEquals(expectedPrice1, product.getPrice());
            } else if (product.getName().equals("Product 2")) {
                assertEquals(expectedPrice2, product.getPrice());
            }
        }
    }
}
