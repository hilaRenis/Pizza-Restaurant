package pizza.restaurant.domain.seeder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pizza.restaurant.domain.seeder.entity.Migration;

/**
 * Migration repository
 */
@Repository
public interface MigrationRepository extends JpaRepository<Migration, Long> {
    /**
     * Find Migration by methodName
     *
     * @param methodName
     * @return
     */
    Migration findByMethodName(String methodName);
}
