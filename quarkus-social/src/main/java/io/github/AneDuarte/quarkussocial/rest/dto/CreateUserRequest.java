package io.github.AneDuarte.quarkussocial.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//Ou @Data que inclui ambos e outros
public class CreateUserRequest {
    @NotBlank(message = "Name is Required")
    private String name;
    @NotNull(message = "Age is Required")
    private Integer age;
}
