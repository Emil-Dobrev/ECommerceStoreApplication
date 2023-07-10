package emildobrev.ecommerce.store.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import emildobrev.ecommerce.store.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

@Data
@Document(collection = "products")
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    private String id;
    @NonNull
    private String name;
    @NonNull
    private String description;
    @NonNull
    private BigDecimal price;
    private int quantityInWarehouse;
    private int orderQuantity = 1;
    private List<Comment> comments = new ArrayList<>();
    private Category category;
    private double rating;
    private HashMap<String, Double> votedUsers = new HashMap<>();


    public void addComment(Comment comment){
        this.comments.add(comment);
    }
    public void addVote(String id, Double rating) {
        votedUsers.put(id, rating);
    }
    public void removeVote(String id) {
        votedUsers.remove(id);
    }
}
