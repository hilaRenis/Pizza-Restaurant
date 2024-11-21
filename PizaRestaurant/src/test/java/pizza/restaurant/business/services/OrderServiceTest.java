package pizza.restaurant.business.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pizza.restaurant.application.exception.AppException;
import pizza.restaurant.domain.entity.Order;
import pizza.restaurant.domain.entity.OrderStatus;
import pizza.restaurant.domain.repository.OrderRepository;
import pizza.restaurant.domain.service.StatisticsService;
import pizza.restaurant.presantation.response.StatisticsResponse;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private StatisticsService statisticsService;

    @Test
    void testGenerateStatsWithValidDates() {
        // Setup
        Date startDate = new Date(); // Set appropriate test dates
        Date endDate = new Date();

        Order order = new Order();
        order.setStatus(OrderStatus.PLACED);

        when(orderRepository.findAllByCreatedAtIsBetween(any(Date.class), any(Date.class)))
                .thenReturn(List.of(order)); // Assuming Order is a proper entity

        when(orderRepository.totalSalesMoneyBetweenDate(any(Date.class), any(Date.class)))
                .thenReturn(100.0);

        when(orderRepository.numberOfOrdersBetweenDate(any(Date.class), any(Date.class)))
                .thenReturn(10L);

        when(orderRepository.avgOrderValueBetweenDate(any(Date.class), any(Date.class)))
                .thenReturn(10.0);

        // Execute
        StatisticsResponse stats = statisticsService.generateStats(startDate, endDate);

        // Verify
        assertNotNull(stats);
    }


    @Test
    void testGenerateStatsWithStartDateAfterEndDate() {
        // Setup
        Date startDate = new Date(); // Set dates where startDate is after endDate
        Date endDate = new Date(startDate.getTime() - 1);

        // Execute & Verify
        assertThrows(AppException.class, () -> statisticsService.generateStats(startDate, endDate));
    }

}
