package com.github.tanakakfuji.vending_machine_api.controller.vm;

import com.github.tanakakfuji.vending_machine_api.domain.model.vm.VendingMachine;
import com.github.tanakakfuji.vending_machine_api.input.vm.PaymentInput;
import com.github.tanakakfuji.vending_machine_api.response.ChangeResponse;
import com.github.tanakakfuji.vending_machine_api.service.vm.VendingMachineService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vending-machines")
public class VendingMachineController {
    private final VendingMachineService vendingMachineService;

    public VendingMachineController(VendingMachineService vendingMachineService) {
        this.vendingMachineService = vendingMachineService;
    }

    @GetMapping
    public List<VendingMachine> getVendingMachines() {
        return vendingMachineService.findOpenVms();
    }

    @PostMapping("/{vmId}/purchase/{drinkId}")
    public ChangeResponse purchaseDrink(@PathVariable Integer vmId, @PathVariable Integer drinkId, @RequestBody @Validated PaymentInput paymentInput) {
        int change = vendingMachineService.purchaseDrink(vmId, drinkId, paymentInput);
        return new ChangeResponse(change);
    }
}
