package pizza.restaurant.domain.service;

import org.springframework.stereotype.Service;
import pizza.restaurant.domain.entity.Order;
import pizza.restaurant.domain.repository.OrderRepository;

/**
 * Order Service
 */
@Service
public class OrderService extends BaseService<Order, OrderRepository, Long> {

    /**
     * Default Required constructor
     *
     * @param baseRepository repository
     */
    public OrderService(OrderRepository baseRepository) {
        super(baseRepository);
    }
}
