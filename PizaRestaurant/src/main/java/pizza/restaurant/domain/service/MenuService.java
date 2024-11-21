package pizza.restaurant.domain.service;

import org.springframework.stereotype.Service;
import pizza.restaurant.domain.entity.Menu;
import pizza.restaurant.domain.repository.MenuRepository;

@Service
public class MenuService extends BaseService<Menu, MenuRepository, Long> {
    /**
     * Default Required constructor
     *
     * @param baseRepository repository
     */
    public MenuService(MenuRepository baseRepository) {
        super(baseRepository);
    }
}
