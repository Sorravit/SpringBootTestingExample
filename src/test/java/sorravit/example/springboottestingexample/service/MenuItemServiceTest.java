package sorravit.example.springboottestingexample.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sorravit.example.springboottestingexample.model.MenuItem;
import sorravit.example.springboottestingexample.model.Restaurant;
import sorravit.example.springboottestingexample.repository.MenuItemRepository;
import sorravit.example.springboottestingexample.repository.RestaurantRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuItemServiceTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    private MenuItemService menuItemService;

    @BeforeEach
    void setUp() {
        menuItemService = new MenuItemService(menuItemRepository, restaurantRepository);
    }

    @Test
    void addMenuItem_WithValidRestaurant_ShouldReturnMenuItem() {
        // Arrange
        Restaurant restaurant = new Restaurant(1L, "Test Restaurant");
        MenuItem menuItem = new MenuItem(null, 1L, "Test Item", 10.0);

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        doAnswer(invocation -> {
            MenuItem item = invocation.getArgument(0);
            item.setId(1L); // Simulate generated ID
            return item;
        }).when(menuItemRepository).insert(any(MenuItem.class));

        // Act
        MenuItem created = menuItemService.addMenuItem(menuItem);

        // Assert
        assertThat(created.getId()).isEqualTo(1L);
        assertThat(created.getName()).isEqualTo("Test Item");
        assertThat(created.getPrice()).isEqualTo(10.0);
    }

    @Test
    void addMenuItem_WithInvalidRestaurant_ShouldThrowException() {
        // Arrange
        MenuItem menuItem = new MenuItem(null, 1L, "Test Item", 10.0);
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> menuItemService.addMenuItem(menuItem))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Restaurant not found");
    }

    @Test
    void getMenuItemsByRestaurant_WithValidRestaurant_ShouldReturnList() {
        // Arrange
        Restaurant restaurant = new Restaurant(1L, "Test Restaurant");
        List<MenuItem> menuItems = Arrays.asList(
            new MenuItem(1L, 1L, "Item 1", 10.0),
            new MenuItem(2L, 1L, "Item 2", 15.0)
        );

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(menuItemRepository.findByRestaurantId(1L)).thenReturn(menuItems);

        // Act
        List<MenuItem> result = menuItemService.getMenuItemsByRestaurant(1L);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Item 1");
        assertThat(result.get(1).getName()).isEqualTo("Item 2");
    }

    @Test
    void getMenuItem_WhenExists_ShouldReturnMenuItem() {
        // Arrange
        MenuItem menuItem = new MenuItem(1L, 1L, "Test Item", 10.0);
        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(menuItem));

        // Act
        MenuItem result = menuItemService.getMenuItem(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Test Item");
    }

    @Test
    void getMenuItem_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(menuItemRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> menuItemService.getMenuItem(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Menu item not found");
    }
}
