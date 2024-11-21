package pizza.restaurant.security.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import pizza.restaurant.domain.entity.Order;

import java.util.List;

@Setter
@Getter
@Entity
public class Customer extends User {
    String fullName;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Order> orders;
}
