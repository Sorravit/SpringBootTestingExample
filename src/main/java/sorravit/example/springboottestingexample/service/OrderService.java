package sorravit.example.springboottestingexample.service;

import org.springframework.stereotype.Service;
import sorravit.example.springboottestingexample.exception.OrderNotFoundException;
import sorravit.example.springboottestingexample.exception.RestaurantNotFoundException;
import sorravit.example.springboottestingexample.model.MenuItem;
import sorravit.example.springboottestingexample.model.Order;
import sorravit.example.springboottestingexample.model.OrderItem;
import sorravit.example.springboottestingexample.model.OrderStatus;
import sorravit.example.springboottestingexample.repository.OrderRepository;
import sorravit.example.springboottestingexample.repository.RestaurantRepository;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuItemService menuItemService;

    public OrderService(OrderRepository orderRepository, RestaurantRepository restaurantRepository, MenuItemService menuItemService) {
        this.orderRepository = orderRepository;
        this.restaurantRepository = restaurantRepository;
        this.menuItemService = menuItemService;
    }

    public Order createOrder(Order order) {
        validateOrder(order);
        calculateTotalPrice(order);
        order.setStatus(OrderStatus.PENDING);
        orderRepository.insert(order);
        return order;
    }

    public Order getOrder(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id " + id));
    }

    public Order updateOrderStatus(Long id, OrderStatus status) {
        Order order = getOrder(id);
        orderRepository.updateStatus(id, status);
        order.setStatus(status);
        return order;
    }

    private void validateOrder(Order order) {
        restaurantRepository.findById(order.getRestaurantId())
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found with id " + order.getRestaurantId()));

        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new RuntimeException("Order must contain at least one item");
        }
    }

    private void calculateTotalPrice(Order order) {
        double total = 0.0;
        for (OrderItem item : order.getItems()) {
            MenuItem menuItem = menuItemService.getMenuItem(item.getMenuItemId());
            item.setPrice(menuItem.getPrice() * item.getQuantity());
            total += item.getPrice();
        }
        order.setTotalPrice(total);
    }
}
