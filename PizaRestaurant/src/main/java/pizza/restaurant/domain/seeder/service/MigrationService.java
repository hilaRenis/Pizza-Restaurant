package pizza.restaurant.domain.seeder.service;

import org.springframework.stereotype.Service;
import pizza.restaurant.domain.seeder.entity.Migration;
import pizza.restaurant.domain.seeder.repository.MigrationRepository;

/**
 * Migration Service
 */
@Service
public class MigrationService {
    /**
     * MigrationRepository instance
     */
    private final MigrationRepository migrationRepository;

    /**
     * Dependency injection
     *
     * @param migrationsRepository
     */
    public MigrationService(MigrationRepository migrationsRepository) {
        this.migrationRepository = migrationsRepository;
    }

    /**
     * Method that saves a new Migration
     *
     * @param methodName
     * @return
     */
    public Migration save(String methodName) {
        Migration newMigration = new Migration();
        newMigration.setMethodName(methodName);
        return migrationRepository.save(newMigration);
    }

    /**
     * Method that gets a migration by methodName
     *
     * @param methodName
     * @return
     */
    public Migration get(String methodName) {
        return this.migrationRepository.findByMethodName(methodName);
    }

}

