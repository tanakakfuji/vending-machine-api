package com.github.tanakakfuji.vending_machine_api.input.drink;

import jakarta.validation.constraints.*;

public record DrinkInput(
        @NotBlank
        @Size(min = 1, max = 100)
        String name,
        @NotNull
        @Min(1)
        @Max(10000)
        Integer volume,
        @NotNull
        @Min(0)
        @Max(10000)
        Integer price,
        @NotNull
        @Min(0)
        @Max(1000)
        Integer stock
) {
}
