package pizza.restaurant.domain.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pizza.restaurant.domain.entity.Order;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends BaseRepository<Order, Long> {

    List<Order> findAllByCreatedAtIsBetween(Date startDate, Date endDate);

    @Query("SELECT AVG (or.totalPrice) FROM Order or WHERE or.createdAt BETWEEN :startDate AND :endDate")
    Double avgOrderValueBetweenDate(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT SUM (or.totalPrice) FROM Order or WHERE or.createdAt BETWEEN :startDate AND :endDate")
    Double totalSalesMoneyBetweenDate(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT COUNT (or) FROM Order or WHERE or.createdAt BETWEEN :startDate AND :endDate")
    Long numberOfOrdersBetweenDate(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
