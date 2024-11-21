package pizza.restaurant.security.response;

import lombok.Data;
import pizza.restaurant.security.entity.Role;

@Data
public class UserResponse {
    /**
     * User id
     */
    private Long id;

    /**
     * Username
     */
    private String username;

    private String fullName;

    private Role role;
}
