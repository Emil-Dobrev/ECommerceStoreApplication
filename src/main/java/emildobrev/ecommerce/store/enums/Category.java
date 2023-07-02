package emildobrev.ecommerce.store.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Category {
    @JsonProperty("Electronic")
    ELECTRONIC,
    @JsonProperty("Clothing")
    CLOTHING,
    @JsonProperty("Books")
    BOOKS,
    @JsonProperty("Home Appliances")
    HOME_APPLIANCES,
    @JsonProperty("Sports & Outdoors")
    SPORTS_AND_OUTDOORS,
    @JsonProperty("Beauty & Personal Care")
    BEAUTY_AND_PERSONAL_CARE,
    @JsonProperty("Health & Wellness")
    HEALTH_AND_WELLNESS,
    @JsonProperty("Toys & Games")
    TOYS_AND_GAMES,
    @JsonProperty("Home Decor")
    HOME_DECOR,
    @JsonProperty("Furniture")
    FURNITURE,
    @JsonProperty("Food & Drinks")
    FOOD_AND_DRINKS,
    @JsonProperty("Automotive")
    AUTOMOTIVE,
    @JsonProperty("Pet Supplies")
    PET_SUPPLIES,
    @JsonProperty("Music & Movies")
    MUSIC_AND_MOVIES;
}
