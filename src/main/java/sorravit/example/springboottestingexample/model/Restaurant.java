package sorravit.example.springboottestingexample.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Restaurant {
    private Long id;

    @NotBlank(message = "Restaurant name is required")
    private String name;
}

