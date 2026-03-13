package com.github.tanakakfuji.vending_machine_api.domain.service.vm;

import com.github.tanakakfuji.vending_machine_api.domain.model.vm.Name;
import com.github.tanakakfuji.vending_machine_api.repository.VendingMachineRepository;
import org.springframework.stereotype.Component;

@Component
public class VendingMachineDomainService {
    private final VendingMachineRepository vendingMachineRepository;

    public VendingMachineDomainService(VendingMachineRepository vendingMachineRepository) {
        this.vendingMachineRepository = vendingMachineRepository;
    }

    public void checkDuplicateName(Name name) {
        if (name == null) throw new IllegalArgumentException("自販機の名前を指定してください。");
        if (vendingMachineRepository.existsByName(name))
            throw new IllegalArgumentException("入力された名前の自販機が既に存在します。重複しない名前を入力してください。");
    }

    public void checkDuplicateNameExcludingId(Name name, Integer id) {
        if (name == null) throw new IllegalArgumentException("自販機の名前を指定してください。");
        if (id == null) throw new IllegalArgumentException("自販機を指定してください。");
        if (vendingMachineRepository.existsByNameAndIdNot(name, id))
            throw new IllegalArgumentException("入力された名前の自販機が他に存在します。重複しない名前を入力してください。");
    }
}
