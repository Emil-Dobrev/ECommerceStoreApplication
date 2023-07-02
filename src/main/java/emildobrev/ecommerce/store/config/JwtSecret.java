package emildobrev.ecommerce.store.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
public class JwtSecret {
     @Value("${secret}")
     public  String secret;
}
