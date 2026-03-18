package com.github.tanakakfuji.vending_machine_api.input.drink;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record DrinkListInput(
        @NotNull
        @Size(min = 1, max = 100)
        List<@NotNull @Valid DrinkInput> drinks
) {
}
