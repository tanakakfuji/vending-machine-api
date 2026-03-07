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

    public boolean isDuplicateName(Name name) {
        if (name == null) throw new IllegalArgumentException("自販機の名前を指定してください。");
        return vendingMachineRepository.existsByName(name);
    }

    public boolean isDuplicateNameExcludingId(Name name, Integer id) {
        if (name == null) throw new IllegalArgumentException("自販機の名前を指定してください。");
        if (id == null) throw new IllegalArgumentException("自販機を指定してください。");
        return vendingMachineRepository.existsByNameAndIdNot(name, id);
    }
}
