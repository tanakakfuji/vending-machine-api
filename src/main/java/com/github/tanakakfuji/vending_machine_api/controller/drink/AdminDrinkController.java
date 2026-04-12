package com.github.tanakakfuji.vending_machine_api.controller.drink;

import com.github.tanakakfuji.vending_machine_api.input.drink.DrinkInput;
import com.github.tanakakfuji.vending_machine_api.input.drink.DrinkListInput;
import com.github.tanakakfuji.vending_machine_api.service.drink.AdminDrinkService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/vending-machines/{vmId}/drinks")
public class AdminDrinkController {
    private final AdminDrinkService adminDrinkService;

    public AdminDrinkController(AdminDrinkService adminDrinkService) {
        this.adminDrinkService = adminDrinkService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void registerDrink(@PathVariable Integer vmId, @RequestBody @Validated DrinkListInput input) {
        adminDrinkService.create(vmId, input);
    }

    @PutMapping("/{drinkId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateDrink(@PathVariable Integer vmId, @PathVariable Integer drinkId, @RequestBody @Validated DrinkInput input) {
        adminDrinkService.update(vmId, drinkId, input);
    }

    @DeleteMapping("/{drinkId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDrink(@PathVariable Integer vmId, @PathVariable Integer drinkId) {
        adminDrinkService.deleteById(vmId, drinkId);
    }
}
