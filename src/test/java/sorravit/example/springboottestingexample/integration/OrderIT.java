package sorravit.example.springboottestingexample.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import sorravit.example.springboottestingexample.TestUtils;
import sorravit.example.springboottestingexample.model.MenuItem;
import sorravit.example.springboottestingexample.model.Order;
import sorravit.example.springboottestingexample.model.Restaurant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@Execution(ExecutionMode.CONCURRENT)
class OrderIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private Restaurant restaurant;
    private MenuItem menuItem;
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

    @BeforeEach
    void setup() throws Exception {
        // Create restaurant
        restaurant = TestUtils.createSampleRestaurant("Full Flow Restaurant");
        var r = mockMvc.perform(post("/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(restaurant)))
                .andExpect(status().isOk())
                .andReturn();
        restaurant = objectMapper.readValue(r.getResponse().getContentAsString(), Restaurant.class);

        // Create menu item
        menuItem = TestUtils.createSampleMenuItem("Burger", 10.0);
        var m = mockMvc.perform(post("/restaurants/" + restaurant.getId() + "/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuItem)))
                .andExpect(status().isOk())
                .andReturn();
        menuItem = objectMapper.readValue(m.getResponse().getContentAsString(), MenuItem.class);
    }

    @Test
    void fullFlow_CreateOrderAndUpdateStatus() throws Exception {
        // Create order
        Order order = TestUtils.createSampleOrder(restaurant.getId(), menuItem.getId(), 2);

        var createOrderResult = mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andReturn();

        Order createdOrder = objectMapper.readValue(createOrderResult.getResponse().getContentAsString(), Order.class);

        // Update order status
        mockMvc.perform(put("/orders/" + createdOrder.getId() + "/status")
                        .param("status", "DELIVERED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DELIVERED"));
    }
}
