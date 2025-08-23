package sorravit.example.springboottestingexample.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import sorravit.example.springboottestingexample.model.MenuItem;
import sorravit.example.springboottestingexample.model.Restaurant;
import sorravit.example.springboottestingexample.repository.MenuItemRepository;
import sorravit.example.springboottestingexample.repository.RestaurantRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@SpringBootTest
@Execution(ExecutionMode.CONCURRENT)
class MenuItemRepositoryIT {

    @Container
    static MySQLContainer<?> mysql =
            new MySQLContainer<>("mysql:8.0")
                    .withDatabaseName("test")
                    .withUsername("test")
                    .withPassword("test");

    @DynamicPropertySource
    static void configureDatasource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }
//    Can be move to a base class if there are many integration tests
//    But for simplicity, keep it here for now

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    void testInsertAndFind() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");
        restaurantRepository.insert(restaurant);

        MenuItem item = new MenuItem();
        item.setRestaurantId(1L);
        item.setName("Green Curry");
        item.setPrice(150.0);

        menuItemRepository.insert(item);

        Optional<MenuItem> found = menuItemRepository.findById(item.getId());
        assertTrue(found.isPresent());
        assertEquals("Green Curry", found.get().getName());
        assertEquals(150.0, found.get().getPrice());
    }
}
