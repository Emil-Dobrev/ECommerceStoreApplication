package emildobrev.Ecommerce.Store.email;

import emildobrev.Ecommerce.Store.order.EmailMetaInformation;

public interface EmailService {

        void sendEmail(EmailMetaInformation emailMetaInformation);
}
