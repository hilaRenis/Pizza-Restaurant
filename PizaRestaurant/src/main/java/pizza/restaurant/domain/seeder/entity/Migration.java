package pizza.restaurant.domain.seeder.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import pizza.restaurant.domain.entity.Model;


/**
 * Migration entity
 */
@Entity
@Getter
@Setter
@Table(name = "migrations")
public class Migration extends Model<Long> {

    /**
     * Method name
     */
    private String methodName;

}
