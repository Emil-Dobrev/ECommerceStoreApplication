package emildobrev.ecommerce.store.email;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.context.ApplicationEvent;
import org.springframework.data.annotation.Transient;

@Value
@EqualsAndHashCode(callSuper = true)
public class EmailEvent<T> extends ApplicationEvent {

    T value;

    public EmailEvent(Object source, T value) {
        super(source);
        this.value = value;
    }
}
