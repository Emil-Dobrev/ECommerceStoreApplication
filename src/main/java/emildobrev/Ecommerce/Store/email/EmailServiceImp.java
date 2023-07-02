package emildobrev.Ecommerce.Store.email;

import emildobrev.Ecommerce.Store.order.EmailMetaInformation;
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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImp implements EmailService {

    public static final String TEXT_PLACEHOLDER = "TEXT_PLACEHOLDER";
    public static final String PLACEHOLDER_TITLE = "PLACEHOLDER_TITLE";
    public static final String PLACEHOLDER_HEADER = "PLACEHOLDER_HEADER";
    private final String SENDER_EMAIL = "dobrev93sl@gmail.com";
    private static final String STATIC_EMAIL_TEMPLATE_HTML = "static/EmailTemplate.html";
    private final JavaMailSender javaMailSender;


    @Override
    @Async
    public void sendEmail(@NonNull EmailMetaInformation emailMetaInformation) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            mimeMessage.setFrom(new InternetAddress(SENDER_EMAIL));
            helper.setText(buildEmail(emailMetaInformation), true);
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(emailMetaInformation.getEmail()));
            helper.setSubject(emailMetaInformation.getSubject());
            javaMailSender.send(mimeMessage);
        } catch (MessagingException | RuntimeException | IOException e) {
            log.error("Failed to send email: {}" + e.getMessage());
        }
    }


    private String buildEmail(EmailMetaInformation emailMetaInformation) throws IOException {
        return StreamUtils.copyToString(new ClassPathResource(STATIC_EMAIL_TEMPLATE_HTML).getInputStream(), StandardCharsets.UTF_8)
                .replace(PLACEHOLDER_TITLE, emailMetaInformation.getTitle())
                .replace(PLACEHOLDER_HEADER, emailMetaInformation.getHeader())
                .replace(TEXT_PLACEHOLDER, emailMetaInformation.getText());
    }
}
