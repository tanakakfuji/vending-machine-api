package com.github.tanakakfuji.vending_machine_api.domain.service.drink;

import com.github.tanakakfuji.vending_machine_api.repository.VendingMachineRepository;
import org.springframework.stereotype.Component;

@Component
public class DrinkDomainService {
    private final VendingMachineRepository vendingMachineRepository;

    public DrinkDomainService(VendingMachineRepository vendingMachineRepository) {
        this.vendingMachineRepository = vendingMachineRepository;
    }

    public boolean existsByVmId(Integer VmId) {
        if (VmId == null) throw new IllegalArgumentException("対象の自販機が正しく指定されていません。");
        return vendingMachineRepository.existsById(VmId);
    }
}
