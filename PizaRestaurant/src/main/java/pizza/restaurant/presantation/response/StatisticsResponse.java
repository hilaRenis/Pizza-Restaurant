package pizza.restaurant.presantation.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import pizza.restaurant.domain.entity.OrderStatus;

import java.util.List;
import java.util.Map;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class StatisticsResponse {
    private Double totalSalesMoney;
    private Long orderCount;
    private Double averageOrderValue;
    private Map<OrderStatus, Long> orderStatusDistribution; // Map of OrderStatus to its Count
    private List<Map<String, Object>> mostOrderedItems;

}
