package pizza.restaurant.presantation.response;

import lombok.Data;

@Data
public class MenuResponse {
    private Integer id;
    private String article;
    private String description;
    private double price;
}
