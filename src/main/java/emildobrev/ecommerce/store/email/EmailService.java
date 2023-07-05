package emildobrev.ecommerce.store.email;

public interface EmailService {

        <T>  void sendEmail(EmailMetaInformation emailMetaInformation, T object);
}
