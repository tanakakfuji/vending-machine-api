package com.github.tanakakfuji.vending_machine_api.service.vm;

import com.github.tanakakfuji.vending_machine_api.domain.model.drink.Drink;
import com.github.tanakakfuji.vending_machine_api.domain.model.vm.Status;
import com.github.tanakakfuji.vending_machine_api.domain.model.vm.VendingMachine;
import com.github.tanakakfuji.vending_machine_api.input.vm.PaymentInput;
import com.github.tanakakfuji.vending_machine_api.repository.VendingMachineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Transactional
public class VendingMachineService {
    private final VendingMachineRepository vendingMachineRepository;

    public VendingMachineService(VendingMachineRepository vendingMachineRepository) {
        this.vendingMachineRepository = vendingMachineRepository;
    }

    public List<VendingMachine> findOpenVms() {
        return vendingMachineRepository.findByStatus(Status.OPEN);
    }

    public int purchase(Integer vmId, Integer drinkId, PaymentInput paymentInput) {
        if (vmId == null) throw new NullPointerException("自販機のidが不正な値です。");
        if (drinkId == null) throw new NullPointerException("飲み物のidが不正な値です。");
        if (paymentInput == null) throw new NullPointerException("投入金額が不正な値です。");
        VendingMachine vendingMachine = vendingMachineRepository.findById(vmId)
                .orElseThrow(() -> new IllegalArgumentException("指定された自販機は存在しません。"));
        vendingMachine.checkOpened();
        Set<Drink> drinks = vendingMachine.getDrinks();
        Drink drink = drinks.stream().filter(d -> d.getId().equals(drinkId)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("指定された飲み物は存在しません。"));
        int change = drink.calculateChange(paymentInput.money());
        drink.decrementStock();
        vendingMachineRepository.save(vendingMachine);
        return change;
    }
}
