package sorravit.example.springboottestingexample.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Restaurant {
    private Long id;

    @NotBlank(message = "Restaurant name is required")
    private String name;
}

