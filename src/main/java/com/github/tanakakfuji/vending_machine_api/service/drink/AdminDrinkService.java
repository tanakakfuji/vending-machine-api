package com.github.tanakakfuji.vending_machine_api.service.drink;

import com.github.tanakakfuji.vending_machine_api.domain.model.drink.*;
import com.github.tanakakfuji.vending_machine_api.domain.model.vm.VendingMachine;
import com.github.tanakakfuji.vending_machine_api.input.drink.DrinkInput;
import com.github.tanakakfuji.vending_machine_api.input.drink.DrinkListInput;
import com.github.tanakakfuji.vending_machine_api.repository.VendingMachineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdminDrinkService {
    private final VendingMachineRepository vendingMachineRepository;

    public AdminDrinkService(VendingMachineRepository vendingMachineRepository) {
        this.vendingMachineRepository = vendingMachineRepository;
    }

    public void create(Integer vmId, DrinkListInput input) {
        if (vmId == null) throw new NullPointerException("自販機を指定してください。");
        if (input == null) throw new NullPointerException("登録飲み物が不正な値です。");
        VendingMachine vm = vendingMachineRepository.findById(vmId)
                .orElseThrow(() -> new IllegalArgumentException("指定された自販機は存在しません。"));
        Set<Drink> drinkSet = input.drinks().stream()
                .map(d -> Drink.create(vmId, new Name(d.name()), new Volume(d.volume()), new Price(d.price()), new Stock(d.stock())))
                .collect(Collectors.toSet());
        vm.addDrinks(drinkSet);
        vendingMachineRepository.save(vm);
    }

    public void update(Integer vmId, Integer drinkId, DrinkInput drinkInput) {
        if (vmId == null) throw new NullPointerException("自販機を指定してください。");
        if (drinkId == null) throw new NullPointerException("飲み物を指定してください。");
        if (drinkInput == null) throw new NullPointerException("登録飲み物が不正な値です。");
        VendingMachine vm = vendingMachineRepository.findById(vmId)
                .orElseThrow(() -> new IllegalArgumentException("指定された自販機は存在しません。"));
        Set<Drink> drinks = vm.getDrinks();
        Drink drink = drinks.stream().filter(d -> d.getId().equals(drinkId)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("指定された飲み物は存在しません。"));
        boolean isDuplicateName = drinks.stream().anyMatch(d -> d.getName().value().equals(drinkInput.name()) && !d.getId().equals(drink.getId()));
        if (isDuplicateName) throw new IllegalArgumentException("自販機内で飲み物の名前が重複します。");
        drink.update(new Name(drinkInput.name()), new Volume(drinkInput.volume()), new Price(drinkInput.price()), new Stock(drinkInput.stock()));
        vendingMachineRepository.save(vm);
    }

    public void deleteById(Integer vmId, Integer drinkId) {
        if (vmId == null) throw new NullPointerException("自販機を指定してください。");
        if (drinkId == null) throw new NullPointerException("飲み物を指定してください。");
        VendingMachine vm = vendingMachineRepository.findById(vmId)
                .orElseThrow(() -> new IllegalArgumentException("指定された自販機は存在しません。"));
        Set<Drink> drinks = vm.getDrinks();
        boolean removed = drinks.removeIf(d -> d.getId().equals(drinkId));
        if (!removed) throw new IllegalArgumentException("指定された飲み物は存在しません。");
        vendingMachineRepository.save(vm);
    }
}
