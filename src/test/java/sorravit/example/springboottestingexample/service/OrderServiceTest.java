package sorravit.example.springboottestingexample.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sorravit.example.springboottestingexample.exception.OrderNotFoundException;
import sorravit.example.springboottestingexample.exception.RestaurantNotFoundException;
import sorravit.example.springboottestingexample.model.*;
import sorravit.example.springboottestingexample.repository.OrderRepository;
import sorravit.example.springboottestingexample.repository.RestaurantRepository;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private MenuItemService menuItemService;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(orderRepository, restaurantRepository, menuItemService);
    }

    @Test
    void createOrder_WithValidData_ShouldReturnOrder() {
        // Arrange
        Restaurant restaurant = new Restaurant(1L, "Test Restaurant");
        MenuItem menuItem1 = new MenuItem(1L, 1L, "Item 1", 10.0);
        MenuItem menuItem2 = new MenuItem(2L, 1L, "Item 2", 15.0);

        Order order = new Order();
        order.setRestaurantId(1L);
        order.setItems(Arrays.asList(
            new OrderItem(1L, 2),
            new OrderItem(2L, 1)
        ));

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(menuItemService.getMenuItem(1L)).thenReturn(menuItem1);
        when(menuItemService.getMenuItem(2L)).thenReturn(menuItem2);
        doAnswer(invocation -> {
            Order savedOrder = invocation.getArgument(0);
            savedOrder.setId(1L); // Simulate generated ID
            savedOrder.setTotalPrice(35.0); // Calculate total price
            return null;
        }).when(orderRepository).insert(any(Order.class));

        // Act
        Order created = orderService.createOrder(order);

        // Assert
        assertThat(created.getTotalPrice()).isEqualTo(35.0); // (10.0 * 2) + (15.0 * 1)
        assertThat(created.getStatus()).isEqualTo(OrderStatus.PENDING);
    }

    @Test
    void createOrder_WithInvalidRestaurant_ShouldThrowException() {
        // Arrange
        Order order = new Order();
        order.setRestaurantId(1L);
        order.setItems(Arrays.asList(new OrderItem(1L, 1)));

        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> orderService.createOrder(order))
                .isInstanceOf(RestaurantNotFoundException.class)
                .hasMessage("Restaurant not found with id 1");
    }

    @Test
    void updateOrderStatus_ShouldReturnUpdatedOrder() {
        // Arrange
        Order order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.PENDING);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        // Act
        Order updated = orderService.updateOrderStatus(1L, OrderStatus.DELIVERED);

        // Assert
        assertThat(updated.getStatus()).isEqualTo(OrderStatus.DELIVERED);
    }

    @Test
    void getOrder_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> orderService.getOrder(1L))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessage("Order not found with id 1");
    }
}
