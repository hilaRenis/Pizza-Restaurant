package pizza.restaurant.presantation.controller;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import pizza.restaurant.application.constants.Messages;
import pizza.restaurant.application.mapper.Response;
import pizza.restaurant.domain.entity.Order;
import pizza.restaurant.domain.entity.OrderItem;
import pizza.restaurant.domain.entity.OrderStatus;
import pizza.restaurant.domain.service.OrderService;
import pizza.restaurant.presantation.request.OrderRequest;
import pizza.restaurant.security.service.UserService;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Order Controller
 */
@RestController
@RequestMapping("/orders")
@Slf4j
public class OrderController {

    /**
     * Order service
     */
    private OrderService orderService;


    /**
     * SimpMessagingTemplate template
     */
    private final SimpMessagingTemplate template;

    /**
     * ModelMapper instance
     */
    private final ModelMapper modelMapper;

    private final UserService userService;

    public OrderController(OrderService orderService, SimpMessagingTemplate template, ModelMapper modelMapper, UserService userService) {
        this.orderService = orderService;
        this.template = template;
        this.modelMapper = modelMapper;
        this.userService = userService;
    }


    /**
     * Create an order
     *
     * @param orderRequest
     * @return
     */
    @PostMapping("/customer/create")
    public ResponseEntity<Object> create(@RequestBody OrderRequest orderRequest) {
        Order order = modelMapper.map(orderRequest, Order.class);
        List<OrderItem> items = order.getItems().stream().peek(item -> item.setOrder(order)).toList();
        order.setItems(items);
        order.setStatus(OrderStatus.PLACED);
        order.setCustomer(this.userService.getCurrentCustomer());
        Order savedOrder = orderService.create(order);
        return Response.rest(Messages.SUCCESS_MESSAGE, HttpStatus.CREATED, savedOrder, Messages.RECORDS_RECEIVED);
    }

    /**
     * Get all orders
     *
     * @return
     */
    @GetMapping
    public ResponseEntity<Object> list() {
        List<Order> orders = orderService.list().stream().sorted(Comparator.comparing(order -> !order.getStatus().equals(OrderStatus.PLACED)))
                .collect(Collectors.toList());
        return Response.rest(Messages.SUCCESS_MESSAGE, HttpStatus.OK, orders, Messages.RECORDS_RECEIVED);
    }


    /**
     * Get customer orders
     *
     * @return
     */
    @GetMapping("/my-orders")
    public ResponseEntity<Object> getCustomerOrders() {
        List<Order> orders = this.userService.getCurrentCustomer().getOrders().stream()
                .sorted(Comparator.comparing(order -> !order.getStatus().equals(OrderStatus.PLACED)))
                .collect(Collectors.toList());
        return Response.rest(Messages.SUCCESS_MESSAGE, HttpStatus.OK, orders, Messages.RECORDS_RECEIVED);
    }


    /**
     * Get a single order by ID
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> get(@PathVariable Long id) {
        Order order = orderService.getById(id);

        if (order == null) {
            return Response.rest(Messages.FAIL_MESSAGE, HttpStatus.NOT_FOUND, null, Messages.NOT_FOUND);
        }

        return Response.rest(Messages.SUCCESS_MESSAGE, HttpStatus.ACCEPTED, order, Messages.RECORDS_RECEIVED);
    }

    /**
     * Change order status
     *
     * @param id
     * @param orderStatus
     * @return
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody Map<String, Object> orderStatus) {
        Order order = orderService.getById(id);

        if (order == null) {
            return Response.rest(Messages.FAIL_MESSAGE, HttpStatus.NOT_FOUND, null, Messages.NOT_FOUND);
        }

        order.setStatus(OrderStatus.valueOf(orderStatus.get("status").toString()));
        Order updatedOrder = orderService.update(order, id);

        String message = "Your order is " + order.getStatus().name();
        template.convertAndSendToUser(order.getCustomer().getUsername(), "/queue/notifications", message);


        return Response.rest(Messages.SUCCESS_MESSAGE, HttpStatus.ACCEPTED, updatedOrder, Messages.RECORDS_RECEIVED);
    }

    /**
     * Delete an order
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        Order order = orderService.getById(id);

        if (order == null) {
            return Response.rest(Messages.FAIL_MESSAGE, HttpStatus.NOT_FOUND, null, Messages.NOT_FOUND);
        }

        try {
            orderService.delete(order);
            return Response.rest(Messages.SUCCESS_MESSAGE, HttpStatus.OK, null, Messages.RECORD_DELETED);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            return Response.rest(Messages.FAIL_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR, null, Messages.SERVER_ERROR);
        }
    }
}
