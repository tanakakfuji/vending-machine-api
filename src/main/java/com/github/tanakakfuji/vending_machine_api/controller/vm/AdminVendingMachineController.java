package com.github.tanakakfuji.vending_machine_api.controller.vm;

import com.github.tanakakfuji.vending_machine_api.domain.model.vm.VendingMachine;
import com.github.tanakakfuji.vending_machine_api.input.vm.VendingMachineInput;
import com.github.tanakakfuji.vending_machine_api.service.vm.AdminVendingMachineService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/admin/vending-machines")
public class AdminVendingMachineController {
    private final AdminVendingMachineService adminVendingMachineService;

    public AdminVendingMachineController(AdminVendingMachineService adminVendingMachineService) {
        this.adminVendingMachineService = adminVendingMachineService;
    }

    @GetMapping
    public List<VendingMachine> getVendingMachines() {
        return adminVendingMachineService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> registerVendingMachine(@RequestBody @Validated VendingMachineInput vmInput) {
        VendingMachine vm = adminVendingMachineService.create(vmInput);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(vm.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateVendingMachine(@PathVariable Integer id, @RequestBody @Validated VendingMachineInput vmInput) {
        adminVendingMachineService.update(id, vmInput);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVendingMachine(@PathVariable Integer id) {
        adminVendingMachineService.deleteById(id);
    }
}
