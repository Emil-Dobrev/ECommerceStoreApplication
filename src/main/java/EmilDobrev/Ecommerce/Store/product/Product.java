package EmilDobrev.Ecommerce.Store.product;

import EmilDobrev.Ecommerce.Store.enums.Category;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
@Document(collection = "products")
public class Product {

    @Id
    private String id;
    private String name;
    private String description;
    private double price;
    private int quantity;
    private List<Comments> comments = new ArrayList<>();
    private Category category;
    private double rating;
    private HashMap<String, Double> votedUsers = new HashMap<>();


    public void addComment(Comments comment){
        this.comments.add(comment);
    }
    public void addVote(String email, Double rating) {
        votedUsers.put(email, rating);
    }

    public void removeVote(String email) {
        votedUsers.remove(email);
    }
}
