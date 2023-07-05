package emildobrev.ecommerce.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableMongoRepositories
@EnableMongoAuditing
@EnableScheduling
@EnableAsync
public class ECommerceStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(ECommerceStoreApplication.class, args);
	}

}
