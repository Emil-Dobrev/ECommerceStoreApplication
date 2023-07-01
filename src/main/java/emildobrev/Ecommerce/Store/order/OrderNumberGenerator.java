package emildobrev.Ecommerce.Store.order;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class OrderNumberGenerator {
    private static final String DATE_FORMAT = "yyyyMMddHHmmss";
    private static final int RANDOM_NUMBER_LENGTH = 4;

    public static String generateOrderNumber() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String timestamp = dateFormat.format(new Date());

        String randomNumber = generateRandomNumber(RANDOM_NUMBER_LENGTH);

        return timestamp + randomNumber;
    }

    private static String generateRandomNumber(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int digit = random.nextInt(10);
            sb.append(digit);
        }

        return sb.toString();
    }
}
