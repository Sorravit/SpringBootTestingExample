package sorravit.example.springboottestingexample.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Restaurant {
    private Long id;

    @NotBlank(message = "Restaurant name is required")
    private String name;
}

