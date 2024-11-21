package pizza.restaurant.security.validator;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pizza.restaurant.security.request.UserRequest;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        UserRequest dto = (UserRequest) obj;
        return dto.getPassword() == null || dto.getPassword().equals(dto.getConfirmPassword());
    }
}
