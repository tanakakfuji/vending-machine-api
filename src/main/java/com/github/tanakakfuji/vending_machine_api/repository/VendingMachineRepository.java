package com.github.tanakakfuji.vending_machine_api.repository;

import com.github.tanakakfuji.vending_machine_api.domain.model.vm.Name;
import com.github.tanakakfuji.vending_machine_api.domain.model.vm.VendingMachine;
import org.springframework.data.repository.ListCrudRepository;

public interface VendingMachineRepository extends ListCrudRepository<VendingMachine, Integer> {
    boolean existsByName(Name name);

    boolean existsByNameAndIdNot(Name name, Integer id);
}
