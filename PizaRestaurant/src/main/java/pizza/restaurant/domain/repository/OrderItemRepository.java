package pizza.restaurant.domain.repository;

import org.springframework.stereotype.Repository;
import pizza.restaurant.domain.entity.OrderItem;

@Repository
public interface OrderItemRepository extends BaseRepository<OrderItem, Long> {
}
