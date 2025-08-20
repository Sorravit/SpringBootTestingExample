package sorravit.example.springboottestingexample.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class Order {
    private Long id;
    private Long restaurantId;

    @NotNull(message = "Order items cannot be null")
    @NotEmpty(message = "Order must contain at least one item")
    private List<OrderItem> items;

    private double totalPrice;
    private OrderStatus status;

    public Order() {
        this.status = OrderStatus.PENDING;
    }

}
