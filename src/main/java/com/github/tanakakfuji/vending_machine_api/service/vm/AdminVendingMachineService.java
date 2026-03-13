package com.github.tanakakfuji.vending_machine_api.service.vm;

import com.github.tanakakfuji.vending_machine_api.domain.model.vm.Name;
import com.github.tanakakfuji.vending_machine_api.domain.model.vm.SlotCapacity;
import com.github.tanakakfuji.vending_machine_api.domain.model.vm.VendingMachine;
import com.github.tanakakfuji.vending_machine_api.domain.service.vm.VendingMachineDomainService;
import com.github.tanakakfuji.vending_machine_api.input.vm.VendingMachineInput;
import com.github.tanakakfuji.vending_machine_api.repository.VendingMachineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AdminVendingMachineService {
    private final VendingMachineRepository vendingMachineRepository;
    private final VendingMachineDomainService vendingMachineDomainService;

    public AdminVendingMachineService(VendingMachineRepository vendingMachineRepository, VendingMachineDomainService vendingMachineDomainService) {
        this.vendingMachineRepository = vendingMachineRepository;
        this.vendingMachineDomainService = vendingMachineDomainService;
    }

    public List<VendingMachine> findAll() {
        return vendingMachineRepository.findAll();
    }

    public VendingMachine create(VendingMachineInput vmInput) {
        vendingMachineDomainService.checkDuplicateName(new Name(vmInput.name()));
        VendingMachine vendingMachine = VendingMachine.create(new Name(vmInput.name()), new SlotCapacity(vmInput.slotCapacity()), vmInput.status());
        return vendingMachineRepository.save(vendingMachine);
    }

    public void update(Integer id, VendingMachineInput vmInput) {
        if (id == null) throw new IllegalArgumentException("自販機を指定してください。");
        VendingMachine vendingMachine = vendingMachineRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("指定された自販機は存在しません。"));
        vendingMachineDomainService.checkDuplicateNameExcludingId(new Name(vmInput.name()), id);
        vendingMachine.update(new Name(vmInput.name()), new SlotCapacity(vmInput.slotCapacity()), vmInput.status(), vendingMachine.getDrinks());
        vendingMachineRepository.save(vendingMachine);
    }

    public void deleteById(Integer id) {
        if (id == null) throw new IllegalArgumentException("自販機を指定してください。");
        if (vendingMachineRepository.findById(id).isEmpty())
            throw new IllegalArgumentException("指定された自販機は存在しません。");
        vendingMachineRepository.deleteById(id);
    }
}
