package pizza.restaurant.domain.repository;

import org.springframework.stereotype.Repository;
import pizza.restaurant.domain.entity.Menu;

@Repository
public interface MenuRepository extends BaseRepository<Menu, Long> {
}
