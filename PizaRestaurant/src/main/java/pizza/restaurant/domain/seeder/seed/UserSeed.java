package pizza.restaurant.domain.seeder.seed;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pizza.restaurant.domain.seeder.entity.Migration;
import pizza.restaurant.domain.seeder.service.MigrationService;
import pizza.restaurant.security.entity.Role;
import pizza.restaurant.security.entity.User;
import pizza.restaurant.security.service.UserService;

/**
 * User seed
 */
@Component
public class UserSeed {
    /**
     * UserService instance
     */
    private final UserService userService;

    /**
     * MigrationService instance
     */
    private final MigrationService migrationService;

    /**
     * PasswordEncoder instance
     */
    private final PasswordEncoder bcryptEncoder;

    /**
     * Dependency injection
     *
     * @param userService
     * @param migrationService
     * @param bcryptEncoder
     */
    public UserSeed(UserService userService, MigrationService migrationService, PasswordEncoder bcryptEncoder) {
        this.userService = userService;
        this.migrationService = migrationService;
        this.bcryptEncoder = bcryptEncoder;
    }

    /**
     * Method that seeds the admin when application is firstly executed
     */
    public void run() {
        String methodName = "UserSeed";

        Migration migration = migrationService.get(methodName);
        if (migration != null) {
            return;
        }

        User user = new User();
        user.setUsername("admin");
        user.setPassword(bcryptEncoder.encode("test123"));
        user.setRole(Role.Employee);
        userService.create(user);

        migrationService.save(methodName);
    }
}