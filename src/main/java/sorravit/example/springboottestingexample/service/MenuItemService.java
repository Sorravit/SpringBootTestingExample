package sorravit.example.springboottestingexample.service;

import org.springframework.stereotype.Service;
import sorravit.example.springboottestingexample.model.MenuItem;
import sorravit.example.springboottestingexample.repository.MenuItemRepository;
import sorravit.example.springboottestingexample.repository.RestaurantRepository;

import java.util.List;

@Service
public class MenuItemService {
    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;

    public MenuItemService(MenuItemRepository menuItemRepository, RestaurantRepository restaurantRepository) {
        this.menuItemRepository = menuItemRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public MenuItem addMenuItem(MenuItem menuItem) {
        restaurantRepository.findById(menuItem.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        menuItemRepository.insert(menuItem);
        return menuItem;
    }

    public List<MenuItem> getMenuItemsByRestaurant(Long restaurantId) {
        restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        return menuItemRepository.findByRestaurantId(restaurantId);
    }

    public MenuItem getMenuItem(Long id) {
        return menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu item not found"));
    }
}
