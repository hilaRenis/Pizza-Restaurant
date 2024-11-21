package pizza.restaurant.security.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import pizza.restaurant.domain.entity.Model;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends Model<Long> {
    private String fullName;
    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;
}
