package pizza.restaurant.presantation.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * This class serves as a DTO for Order Item request
 */
@Data
public class OrderItemRequest {

    @NotNull(message = "Item is required!")
    private String article;

    @NotNull(message = "Quantity is required!")
    private Integer quantity;

    @NotNull(message = "Price is required!")
    private Double price;

    @NotNull(message = "Subtotal is required!")
    private Double subtotal;
}
