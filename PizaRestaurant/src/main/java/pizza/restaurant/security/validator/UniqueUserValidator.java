package pizza.restaurant.security.validator;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import pizza.restaurant.application.utils.StringUtils;
import pizza.restaurant.security.repository.UserRepository;

/**
 * This class validates the uniqueness of Usernames
 */
public class UniqueUserValidator implements ConstraintValidator<UniqueUser, String> {
    /**
     * Dependency injection
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Dependency injection
     */
    @Autowired
    HttpServletRequest request;

    /**
     * Method that initializes User validator
     *
     * @param constraintAnnotation
     */
    @Override
    public void initialize(UniqueUser constraintAnnotation) {
        // Do nothing
    }

    /**
     * Method that returns if Username is valid (unique)
     *
     * @param username
     * @param constraintValidatorContext
     * @return
     */
    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        // Update case
        if (request.getMethod().equals("PUT")) {
            Long id = StringUtils.parseIdFromURL(request.getRequestURI());
            return userRepository.findByUsernameAndIdIsNot(username, id).isEmpty();
        }

        // Create case
        return userRepository.findByUsername(username) == null;
    }

}
