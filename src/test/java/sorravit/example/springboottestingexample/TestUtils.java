package sorravit.example.springboottestingexample;


import sorravit.example.springboottestingexample.model.MenuItem;
import sorravit.example.springboottestingexample.model.Order;
import sorravit.example.springboottestingexample.model.OrderItem;
import sorravit.example.springboottestingexample.model.Restaurant;

import java.util.List;

public class TestUtils {

    public static Restaurant createSampleRestaurant(String name) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(name);
        return restaurant;
    }

    public static MenuItem createSampleMenuItem(String name, double price) {
        MenuItem menuItem = new MenuItem();
        menuItem.setName(name);
        menuItem.setPrice(price);
        return menuItem;
    }

    public static Order createSampleOrder(Long restaurantId, Long menuItemId, int quantity) {
        Order order = new Order();
        order.setRestaurantId(restaurantId);
        order.setItems(List.of(new OrderItem(menuItemId, quantity)));
        return order;
    }
}
