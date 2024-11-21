package pizza.restaurant.domain.repository;

import org.springframework.stereotype.Repository;
import pizza.restaurant.security.entity.Customer;

@Repository
public interface CustomerRepository extends BaseRepository<Customer, Long> {
}
