package com.github.tanakakfuji.vending_machine_api.input.vm;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record PaymentInput(
        @Min(0)
        @Max(100000)
        int money
) {
}
