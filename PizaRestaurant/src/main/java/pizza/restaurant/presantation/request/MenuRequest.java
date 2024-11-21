package pizza.restaurant.presantation.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * This class serves as a DTO & validates the Menu article request
 */
@Data
public class MenuRequest {

    @NotEmpty(message = "Article is required!")
    private String article;

    @NotEmpty(message = "Article description is required!")
    private String description;

    @NotNull(message = "Price is required!")
    private double price;

}
