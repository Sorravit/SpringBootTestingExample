package sorravit.example.springboottestingexample.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuItem {
    private Long id;

    @NotNull(message = "Restaurant ID is required")
    private Long restaurantId;

    @NotBlank(message = "Item name is required")
    private String name;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private Double price;

}
