package se.iths.jakartaeelab1.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

public record PersonDto(@NotEmpty String name, @Positive int age, @NotEmpty String profession) {

}
