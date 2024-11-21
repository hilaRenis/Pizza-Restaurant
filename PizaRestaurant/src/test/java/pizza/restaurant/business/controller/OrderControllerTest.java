package pizza.restaurant.business.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import pizza.restaurant.domain.entity.Order;
import pizza.restaurant.domain.entity.OrderStatus;
import pizza.restaurant.domain.service.OrderService;
import pizza.restaurant.presantation.controller.OrderController;
import pizza.restaurant.presantation.request.OrderRequest;
import pizza.restaurant.security.entity.Customer;
import pizza.restaurant.security.service.UserService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {
    @Mock
    private OrderService orderService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private UserService userService;

    @Mock
    private SimpMessagingTemplate template;

    @InjectMocks
    private OrderController orderController;


    @Test
    void testCreateOrder() {
        // Setup
        OrderRequest orderRequest = new OrderRequest(); // Populate with test data
        Order order = new Order(); // Assuming a proper entity
        when(modelMapper.map(orderRequest, Order.class)).thenReturn(order);

        Customer customer = new Customer(); // Assuming Customer is a proper entity
        when(userService.getCurrentCustomer()).thenReturn(customer);

        when(orderService.create(any(Order.class))).thenReturn(order);

        // Execute
        ResponseEntity<Object> response = orderController.create(orderRequest);

        // Verify
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void testListOrders() {
        // Setup
        Order order1 = new Order();
        order1.setStatus(OrderStatus.PLACED);

        Order order2 = new Order();
        order2.setStatus(OrderStatus.PLACED);

        List<Order> orders = Arrays.asList(order1, order2);

        when(orderService.list()).thenReturn(orders);

        // Execute
        ResponseEntity<Object> response = orderController.list();

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetCustomerOrders() {
        // Setup
        Customer customer = mock(Customer.class); // Mocking Customer

        Order order1 = new Order();
        order1.setCustomer(customer);
        order1.setStatus(OrderStatus.PLACED);

        Order order2 = new Order();
        order2.setCustomer(customer);
        order2.setStatus(OrderStatus.PLACED);

        List<Order> orders = Arrays.asList(order1, order2);

        when(userService.getCurrentCustomer()).thenReturn(customer);
        when(customer.getOrders()).thenReturn(orders);

        // Execute
        ResponseEntity<Object> response = orderController.getCustomerOrders();

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @Test
    void testGetOrderByIdFound() {
        // Setup
        Long id = 1L;
        Order order = new Order(); // Assuming a proper entity
        when(orderService.getById(id)).thenReturn(order);

        // Execute
        ResponseEntity<Object> response = orderController.get(id);

        // Verify
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
    }

    @Test
    void testGetOrderByIdNotFound() {
        // Setup
        Long id = 1L;
        when(orderService.getById(id)).thenReturn(null);

        // Execute
        ResponseEntity<Object> response = orderController.get(id);

        // Verify
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testUpdateOrderStatus() {
        // Setup
        Long id = 1L;
        Map<String, Object> orderStatus = Map.of("status", OrderStatus.PLACED.name());

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setUsername("user");

        Order order = new Order(); // Assuming a proper entity
        order.setCustomer(customer);
        when(orderService.getById(id)).thenReturn(order);
        when(orderService.update(any(Order.class), eq(id))).thenReturn(order);

        // Execute
        ResponseEntity<Object> response = orderController.update(id, orderStatus);

        // Verify
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
    }

    @Test
    void testDeleteOrderFound() {
        // Setup
        Long id = 1L;
        Order order = new Order(); // Assuming a proper entity
        when(orderService.getById(id)).thenReturn(order);
        doNothing().when(orderService).delete(order);

        // Execute
        ResponseEntity<Object> response = orderController.delete(id);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Further assertions to check the response body
    }

    @Test
    void testDeleteOrderNotFound() {
        // Setup
        Long id = 1L;
        when(orderService.getById(id)).thenReturn(null);

        // Execute
        ResponseEntity<Object> response = orderController.delete(id);

        // Verify
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        // Further assertions to check the response body
    }

}
