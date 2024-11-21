package pizza.restaurant.security.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Jwt Request
 */
@Getter
@Setter
@NoArgsConstructor
public class LoginRequest implements Serializable {

    @NotBlank(message = "Name is required!")
    private String username;

    @NotBlank(message = "Password is required!")
    private String password;
}
