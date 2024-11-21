package pizza.restaurant.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import pizza.restaurant.domain.entity.Model;

@NoRepositoryBean
public interface BaseRepository<T extends Model, E> extends JpaRepository<T, E> {

}
