package pizza.restaurant.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "menus")
public class Menu extends Model<Long> {
    private String article;
    private String description;
    private double price;
}
