package pizza.restaurant.security.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import pizza.restaurant.security.validator.PasswordMatches;
import pizza.restaurant.security.validator.UniqueUser;

/**
 * This class serves as a DTO & validates the user requests
 */
@Data
@PasswordMatches
public class UserRequest {

    @NotEmpty(message = "Username is required!")
    @UniqueUser(message = "That username is taken. Try another.")
    private String username;

    @NotEmpty(message = "Full name is required!")
    private String fullName;

    @Size(min = 8, message = "Password should be more than 7 characters!")
    private String password;

    private String confirmPassword;
}
