package sorravit.example.springboottestingexample.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderItem {
    private Long menuItemId;
    private Integer quantity;
    private Double price;

    public OrderItem(Long menuItemId, Integer quantity) {
        this.menuItemId = menuItemId;
        this.quantity = quantity;
    }

    @NotNull(message = "Menu item ID is required")
    public Long getMenuItemId() {
        return menuItemId;
    }

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    public Integer getQuantity() {
        return quantity;
    }

}
