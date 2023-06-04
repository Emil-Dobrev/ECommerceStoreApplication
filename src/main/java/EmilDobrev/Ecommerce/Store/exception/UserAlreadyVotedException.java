package EmilDobrev.Ecommerce.Store.exception;

public class UserAlreadyVotedException  extends RuntimeException {
    public UserAlreadyVotedException(String message) {
        super(message);
    }
}
