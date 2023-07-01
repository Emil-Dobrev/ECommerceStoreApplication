package emildobrev.Ecommerce.Store.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@AllArgsConstructor
@Data
@Builder
public class EmailMetaInformation {
    @NonNull
    private String fullName;
    @NonNull
    private String text;
    @NonNull
    private String title;
    @NonNull
    private String subject;
    @NonNull
    private String email;
    @NonNull
    private String header;
}
