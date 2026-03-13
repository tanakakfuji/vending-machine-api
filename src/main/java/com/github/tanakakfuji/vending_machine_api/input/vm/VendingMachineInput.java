package com.github.tanakakfuji.vending_machine_api.input.vm;

import com.github.tanakakfuji.vending_machine_api.domain.model.vm.Status;
import jakarta.validation.constraints.*;

public record VendingMachineInput(
        @NotBlank
        @Size(min = 1, max = 100)
        String name,
        @NotNull
        @Min(0)
        @Max(100)
        Integer slotCapacity,
        @NotNull
        Status status
) {
}
