package pizza.restaurant.security.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import pizza.restaurant.security.validator.UniqueUser;

@Data
public class CustomerRequest {

    @NotBlank(message = "Username is required")
    @UniqueUser(message = "This username is taken. Try another.")
    private String username;

    @NotBlank(message = "Full Name is required")
    private String fullName;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password should be more than 7 characters!")
    private String password;


}
