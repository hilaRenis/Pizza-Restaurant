package pizza.restaurant;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import pizza.restaurant.domain.seeder.seed.UserSeed;

@SpringBootApplication
@EntityScan({
        "pizza.restaurant.domain.entity",
        "pizza.restaurant.security.entity",
        "pizza.restaurant.domain.seeder"
})
@EnableJpaRepositories({
        "pizza.restaurant.domain.repository",
        "pizza.restaurant.security.repository",
        "pizza.restaurant.domain.seeder.repository",
})
public class PizaRestaurantApplication {

    /**
     * UserSeed instance
     */
    private final UserSeed userSeed;

    /**
     * Dependency injection
     *
     * @param userSeed
     */
    public PizaRestaurantApplication(UserSeed userSeed) {
        this.userSeed = userSeed;
    }

    public static void main(String[] args) {
        SpringApplication.run(PizaRestaurantApplication.class, args);
    }

    /**
     * Method that seeds configurations only once after app's execution
     */
    @PostConstruct
    public void seed() {
        userSeed.run();
    }

}
