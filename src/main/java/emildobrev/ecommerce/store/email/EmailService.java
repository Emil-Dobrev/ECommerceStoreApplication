package emildobrev.ecommerce.store.email;

import emildobrev.ecommerce.store.order.EmailMetaInformation;

public interface EmailService {

        void sendEmail(EmailMetaInformation emailMetaInformation);
}
