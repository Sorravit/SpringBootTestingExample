package sorravit.example.springboottestingexample.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sorravit.example.springboottestingexample.model.Restaurant;
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
class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    private RestaurantService restaurantService;

    @BeforeEach
    void setUp() {
        restaurantService = new RestaurantService(restaurantRepository);
    }

    @Test
    void createRestaurant_ShouldReturnRestaurantWithId() {
        // Arrange
        Restaurant restaurant = new Restaurant(null, "Test Restaurant");
        doAnswer(invocation -> {
            Restaurant r = invocation.getArgument(0);
            r.setId(1L); // Simulate generated ID
            return r;
        }).when(restaurantRepository).insert(any(Restaurant.class));

        // Act
        Restaurant created = restaurantService.createRestaurant(restaurant);

        // Assert
        assertThat(created.getId()).isEqualTo(1L);
        assertThat(created.getName()).isEqualTo("Test Restaurant");
    }

    @Test
    void getRestaurant_WhenExists_ShouldReturnRestaurant() {
        // Arrange
        Restaurant restaurant = new Restaurant(1L, "Test Restaurant");
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

        // Act
        Restaurant found = restaurantService.getRestaurant(1L);

        // Assert
        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(1L);
        assertThat(found.getName()).isEqualTo("Test Restaurant");
    }

    @Test
    void getRestaurant_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> restaurantService.getRestaurant(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Restaurant not found");
    }

    @Test
    void getAllRestaurants_ShouldReturnList() {
        // Arrange
        List<Restaurant> restaurants = Arrays.asList(
            new Restaurant(1L, "Restaurant 1"),
            new Restaurant(2L, "Restaurant 2")
        );
        when(restaurantRepository.findAll()).thenReturn(restaurants);

        // Act
        List<Restaurant> result = restaurantService.getAllRestaurants();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Restaurant 1");
        assertThat(result.get(1).getName()).isEqualTo("Restaurant 2");
    }
}
