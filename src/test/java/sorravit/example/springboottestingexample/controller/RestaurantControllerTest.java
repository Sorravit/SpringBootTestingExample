package sorravit.example.springboottestingexample.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sorravit.example.springboottestingexample.model.MenuItem;
import sorravit.example.springboottestingexample.model.Restaurant;
import sorravit.example.springboottestingexample.service.MenuItemService;
import sorravit.example.springboottestingexample.service.RestaurantService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RestaurantController.class)
class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RestaurantService restaurantService;

    @MockBean
    private MenuItemService menuItemService;

    @Test
    void createRestaurant_ShouldReturnCreatedRestaurant() throws Exception {
        Restaurant restaurant = new Restaurant(null, "Test Restaurant");
        Restaurant created = new Restaurant(1L, "Test Restaurant");

        when(restaurantService.createRestaurant(any(Restaurant.class))).thenReturn(created);

        mockMvc.perform(post("/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(restaurant)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Restaurant"));
    }

    @Test
    void getRestaurant_ShouldReturnRestaurant() throws Exception {
        Restaurant restaurant = new Restaurant(1L, "Test Restaurant");
        when(restaurantService.getRestaurant(1L)).thenReturn(restaurant);

        mockMvc.perform(get("/restaurants/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Restaurant"));
    }

    @Test
    void getAllRestaurants_ShouldReturnList() throws Exception {
        List<Restaurant> restaurants = Arrays.asList(
            new Restaurant(1L, "Restaurant 1"),
            new Restaurant(2L, "Restaurant 2")
        );
        when(restaurantService.getAllRestaurants()).thenReturn(restaurants);

        mockMvc.perform(get("/restaurants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Restaurant 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Restaurant 2"));
    }

    @Test
    void addMenuItem_ShouldReturnCreatedMenuItem() throws Exception {
        MenuItem menuItem = new MenuItem(null, null, "Test Item", 10.0);
        MenuItem created = new MenuItem(1L, 1L, "Test Item", 10.0);

        when(menuItemService.addMenuItem(any(MenuItem.class))).thenReturn(created);

        mockMvc.perform(post("/restaurants/1/menu-items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(menuItem)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Item"))
                .andExpect(jsonPath("$.price").value(10.0));
    }

    @Test
    void getRestaurantMenu_ShouldReturnMenuItems() throws Exception {
        List<MenuItem> menuItems = Arrays.asList(
            new MenuItem(1L, 1L, "Item 1", 10.0),
            new MenuItem(2L, 1L, "Item 2", 15.0)
        );
        when(menuItemService.getMenuItemsByRestaurant(1L)).thenReturn(menuItems);

        mockMvc.perform(get("/restaurants/1/menu-items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Item 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Item 2"));
    }
}
