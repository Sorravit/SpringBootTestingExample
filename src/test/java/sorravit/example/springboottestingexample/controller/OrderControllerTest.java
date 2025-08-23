package sorravit.example.springboottestingexample.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import sorravit.example.springboottestingexample.exception.OrderNotFoundException;
import sorravit.example.springboottestingexample.model.Order;
import sorravit.example.springboottestingexample.model.OrderItem;
import sorravit.example.springboottestingexample.model.OrderStatus;
import sorravit.example.springboottestingexample.service.OrderService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    // --- Helper method to perform POST request and return ResultActions ---
    private ResultActions postJson(String url, Object body) throws Exception {
        return mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)));
    }

    // --- Helper method to perform GET request ---
    private ResultActions get(String url) throws Exception {
        return mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get(url));
    }

    // --- Helper method to perform PUT request with params ---
    private ResultActions put(String url, String paramName, String paramValue) throws Exception {
        return mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put(url)
                .param(paramName, paramValue));
    }

    @Test
    void createOrder_ShouldReturnCreatedOrder() throws Exception {
        // Given
        Order orderRequest = new Order();
        orderRequest.setRestaurantId(1L);
        orderRequest.setItems(List.of(new OrderItem(1L, 2)));

        Order createdOrder = new Order();
        createdOrder.setId(1L);
        createdOrder.setRestaurantId(1L);
        createdOrder.setItems(List.of(new OrderItem(1L, 2)));
        createdOrder.setTotalPrice(20.0);
        createdOrder.setStatus(OrderStatus.PENDING);

        when(orderService.createOrder(any(Order.class))).thenReturn(createdOrder);

        // When
        var result = postJson("/orders", orderRequest);

        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.totalPrice").value(20.0))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void getOrder_ShouldReturnOrder() throws Exception {
        // Given
        Order order = new Order();
        order.setId(1L);
        order.setRestaurantId(1L);
        order.setTotalPrice(20.0);
        order.setStatus(OrderStatus.PENDING);

        when(orderService.getOrder(1L)).thenReturn(order);

        // When
        var result = get("/orders/1");

        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.totalPrice").value(20.0))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void updateOrderStatus_ShouldReturnUpdatedOrder() throws Exception {
        // Given
        Order updatedOrder = new Order();
        updatedOrder.setId(1L);
        updatedOrder.setStatus(OrderStatus.DELIVERED);

        when(orderService.updateOrderStatus(eq(1L), eq(OrderStatus.DELIVERED)))
                .thenReturn(updatedOrder);

        // When
        var result = put("/orders/1/status", "status", "DELIVERED");

        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("DELIVERED"));
    }

    @Test
    void createOrder_ShouldReturnBadRequest_WhenRestaurantIdMissing() throws Exception {
        // Given
        Order invalidOrder = new Order(); // restaurantId missing

        // When
        var result = postJson("/orders", invalidOrder);

        // Then
        result.andExpect(status().isBadRequest());
    }

    @Test
    void getOrder_ShouldReturnNotFound_WhenOrderDoesNotExist() throws Exception {
        // Given
        when(orderService.getOrder(999L)).thenThrow(new OrderNotFoundException("Order not found"));

        // When
        var result = get("/orders/999");

        // Then
        result.andExpect(status().isNotFound());
    }

    @Test
    void updateOrderStatus_ShouldReturnBadRequest_WhenStatusInvalid() throws Exception {
        // When
        var result = put("/orders/1/status", "status", "INVALID_STATUS");

        // Then
        result.andExpect(status().isBadRequest());
    }
}
