package io.github.AneDuarte.quarkussocial.rest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreatePostRequest {
    @NotBlank(message = "Text Required")
    private String text;
}
