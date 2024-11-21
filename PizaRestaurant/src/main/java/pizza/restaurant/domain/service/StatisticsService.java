package pizza.restaurant.domain.service;

import org.springframework.stereotype.Service;
import pizza.restaurant.application.exception.AppException;
import pizza.restaurant.domain.entity.Order;
import pizza.restaurant.domain.entity.OrderItem;
import pizza.restaurant.domain.entity.OrderStatus;
import pizza.restaurant.domain.repository.OrderRepository;
import pizza.restaurant.presantation.response.StatisticsResponse;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    private final OrderRepository orderRepository;

    public StatisticsService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


    public StatisticsResponse generateStats(Date startDate, Date endDate) {
        if (startDate.after(endDate)) {
            throw new AppException("Start Date cannot be after end date");
        }
        StatisticsResponse stats = new StatisticsResponse();

        List<Order> ordersBetweenDates = orderRepository.findAllByCreatedAtIsBetween(startDate,endDate);

        Double totalSalesMoney = orderRepository.totalSalesMoneyBetweenDate(startDate,endDate);
        stats.setTotalSalesMoney(totalSalesMoney);

        Long orderCount = orderRepository.numberOfOrdersBetweenDate(startDate,endDate);
        stats.setOrderCount(orderCount);

        //double averageOrderValue = orderCount > 0 ? totalSalesMoney / orderCount : 0;
        Double averageOrderValue = orderRepository.avgOrderValueBetweenDate(startDate, endDate);
        stats.setAverageOrderValue(averageOrderValue);

        Map<OrderStatus, Long> statusDistribution = ordersBetweenDates.stream()
                .collect(Collectors.groupingBy(Order::getStatus, Collectors.counting()));
        stats.setOrderStatusDistribution(statusDistribution);

        List<Map<String, Object>> mostOrderedItems = findMostOrderedItemsWithQuantity(ordersBetweenDates);
        stats.setMostOrderedItems(mostOrderedItems);

        return stats;
    }

    private List<Map<String, Object>> findMostOrderedItemsWithQuantity(List<Order> orders) {
        // Step 1: Collect Frequencies
        Map<String, Long> itemFrequency = orders.stream()
                .flatMap(order -> order.getItems().stream())
                .collect(Collectors.groupingBy(OrderItem::getArticle, Collectors.summingLong(OrderItem::getQuantity)));

        // Step 2 & 3: Sort by Frequency and Collect Items with Quantities
        return itemFrequency.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .map(entry -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("product", entry.getKey());
                    item.put("quantity", entry.getValue());
                    return item;
                })
                .collect(Collectors.toList());
    }
}