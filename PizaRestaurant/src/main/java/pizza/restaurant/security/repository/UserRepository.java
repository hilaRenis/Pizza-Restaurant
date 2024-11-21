package pizza.restaurant.security.repository;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pizza.restaurant.domain.repository.BaseRepository;
import pizza.restaurant.security.entity.Role;
import pizza.restaurant.security.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User, Long> {

    /**
     * This method finds a user by username
     *
     * @param username String
     * @return User
     */
    User findByUsername(String username);

    /**
     * Find User by username by checking all other id-s except the one which is getting updated
     *
     * @param username String
     * @param id       Long
     * @return Optional<User>
     */
    Optional<User> findByUsernameAndIdIsNot(String username, @Param("id") Long id);

    /**
     * Find User by username or role
     *
     * @param username String
     * @param role     String
     * @return Optional<User>
     */
    Optional<User> findByUsernameOrRole(String username, String role);

    /**
     * @param role
     * @return
     */
    List<User> findAllByRole(Role role);
}
