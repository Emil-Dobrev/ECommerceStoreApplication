package emildobrev.Ecommerce.Store.email;

import emildobrev.Ecommerce.Store.coupons.Coupon;
import emildobrev.Ecommerce.Store.order.EmailMetaInformation;
import emildobrev.Ecommerce.Store.order.Order;
import emildobrev.Ecommerce.Store.user.User;
import emildobrev.Ecommerce.Store.user.UserRepository;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Date;

import static emildobrev.Ecommerce.Store.constants.Constants.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImp implements EmailService {

    public static final String TEXT_PLACEHOLDER = "TEXT_PLACEHOLDER";
    public static final String PLACEHOLDER_TITLE = "PLACEHOLDER_TITLE";
    private final String SENDER_EMAIL = "dobrev93sl@gmail.com";
    private static final String STATIC_SIGNAL_EMAIL_TEMPLATE_HTML = "static/CouponTemplate.html";
    private final JavaMailSender javaMailSender;


    @Override
    public void sendEmailForCoupon(@NonNull EmailMetaInformation emailMetaInformation) {

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            mimeMessage.setFrom(new InternetAddress(SENDER_EMAIL));
            helper.setText(buildEmail(emailMetaInformation),  true);
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(emailMetaInformation.getEmail()));
            helper.setSubject(emailMetaInformation.getSubject());
            javaMailSender.send(mimeMessage);
        } catch (MessagingException | RuntimeException | IOException e) {
            log.error("Failed to send email: {}" + e.getMessage());
        }
    }


    private String buildEmail(EmailMetaInformation emailMetaInformation) throws IOException {
        return StreamUtils.copyToString(new ClassPathResource(STATIC_SIGNAL_EMAIL_TEMPLATE_HTML).getInputStream(), StandardCharsets.UTF_8)
                .replace(PLACEHOLDER_TITLE, emailMetaInformation.getTitle())
                .replace(TEXT_PLACEHOLDER, emailMetaInformation.getText());
    }
}
