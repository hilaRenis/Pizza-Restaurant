package pizza.restaurant.presantation.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import pizza.restaurant.domain.entity.OrderStatus;

import java.util.List;

/**
 * This class serves as a DTO & validates the Order request
 */
@Data
public class OrderRequest {

    @NotNull(message = "Customer Is Required!")
    private Long customerId;

    @NotNull(message = "Order items are required!")
    private List<OrderItemRequest> items;

    @NotNull(message = "Total price is required!")
    private Double totalPrice;

    @NotEmpty(message = "Address is required!")
    private String address;

    private OrderStatus status;

}
