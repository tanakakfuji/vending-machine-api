package com.github.tanakakfuji.vending_machine_api.service.vm;

import com.github.tanakakfuji.vending_machine_api.domain.model.drink.Drink;
import com.github.tanakakfuji.vending_machine_api.domain.model.vm.Status;
import com.github.tanakakfuji.vending_machine_api.domain.model.vm.VendingMachine;
import com.github.tanakakfuji.vending_machine_api.input.vm.VendingMachineInput;
import com.github.tanakakfuji.vending_machine_api.repository.VendingMachineRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
@Sql("AdminVendingMachineServiceIntegrationTest.sql")
public class AdminVendingMachineServiceIntegrationTest {
    @Autowired
    AdminVendingMachineService adminVendingMachineService;

    @Autowired
    VendingMachineRepository vendingMachineRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Nested
    class findAllメソッドのテスト {
        @Test
        void 全ての自販機が取得して返される() {
            List<VendingMachine> vendingMachines = vendingMachineRepository.findAll();
            VendingMachine vm1 = vendingMachines.stream().filter(vm -> vm.getId() == 1).findFirst().get();
            Drink d1 = vm1.getDrinks().stream().filter(d -> d.getId() == 1).findFirst().get();
            Drink d2 = vm1.getDrinks().stream().filter(d -> d.getId() == 2).findFirst().get();
            assertEquals("健康自販機", vm1.getName().value());
            assertEquals("オレンジジュース", d1.getName().value());
            assertEquals("水", d2.getName().value());
            VendingMachine vm2 = vendingMachines.stream().filter(vm -> vm.getId() == 2).findFirst().get();
            Drink d3 = vm2.getDrinks().stream().filter(d -> d.getId() == 3).findFirst().get();
            Drink d4 = vm2.getDrinks().stream().filter(d -> d.getId() == 4).findFirst().get();
            assertEquals("目覚まし自販機", vm2.getName().value());
            assertEquals("ブラックコーヒー", d3.getName().value());
            assertEquals("レッドブル", d4.getName().value());
            VendingMachine vm3 = vendingMachines.stream().filter(vm -> vm.getId() == 3).findFirst().get();
            Drink d5 = vm3.getDrinks().stream().filter(d -> d.getId() == 5).findFirst().get();
            Drink d6 = vm3.getDrinks().stream().filter(d -> d.getId() == 6).findFirst().get();
            assertEquals("甘党自販機", vm3.getName().value());
            assertEquals("三ツ矢サイダー", d5.getName().value());
            assertEquals("コカ・コーラ", d6.getName().value());
        }
    }

    @Nested
    @Sql("AdminVendingMachineServiceIntegrationTestOnlyVm.sql")
    class createメソッドのテスト {
        @Test
        void 自販機の名前が重複するとき例外が発生する() {
            String name = "目覚まし自販機";
            VendingMachineInput vmInput = new VendingMachineInput(name, 5, Status.OPEN);
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> adminVendingMachineService.create(vmInput));
            assertEquals("入力された名前の自販機が既に存在します。重複しない名前を入力してください。", exception.getMessage());
        }

        @Test
        void 自販機の名前が重複しないとき自販機が作成される() {
            String name = "重複しない名前";
            Integer slotCapacity = 5;
            Status status = Status.OPEN;
            VendingMachineInput vmInput = new VendingMachineInput(name, slotCapacity, status);
            VendingMachine result = adminVendingMachineService.create(vmInput);
            assertEquals(name, result.getName().value());
            assertEquals(slotCapacity, result.getSlotCapacity().value());
            assertEquals(status, result.getStatus());
            VendingMachine vm = vendingMachineRepository.findById(result.getId()).get();
            assertEquals(name, vm.getName().value());
            assertEquals(slotCapacity, vm.getSlotCapacity().value());
            assertEquals(status, vm.getStatus());
        }
    }

    @Nested
    class updateメソッドのテスト {
        @Test
        void 自販機のidがnullのとき例外が発生する() {
            Integer id = null;
            VendingMachineInput vmInput = new VendingMachineInput("サンプル", 5, Status.OPEN);
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> adminVendingMachineService.update(id, vmInput));
            assertEquals("自販機を指定してください。", exception.getMessage());
        }

        @Test
        void 指定された自販機が存在しないとき例外が発生する() {
            Integer id = 99;
            VendingMachineInput vmInput = new VendingMachineInput("サンプル", 5, Status.OPEN);
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> adminVendingMachineService.update(id, vmInput));
            assertEquals("指定された自販機は存在しません。", exception.getMessage());
        }

        @Test
        void 自販機の名前が他の名前と重複するとき例外が発生する() {
            Integer id = 1;
            VendingMachineInput vmInput = new VendingMachineInput("甘党自販機", 5, Status.OPEN);
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> adminVendingMachineService.update(id, vmInput));
            assertEquals("入力された名前の自販機が他に存在します。重複しない名前を入力してください。", exception.getMessage());
        }

        @Test
        void 自販機の名前が他の名前と重複しないとき値が更新される() {
            Integer id = 1;
            String name = "更新後の名前";
            Integer slotCapacity = 10;
            Status status = Status.CLOSED;
            VendingMachineInput vmInput = new VendingMachineInput(name, slotCapacity, status);
            adminVendingMachineService.update(id, vmInput);
            VendingMachine vm = vendingMachineRepository.findById(id).get();
            assertEquals(name, vm.getName().value());
            assertEquals(slotCapacity, vm.getSlotCapacity().value());
        }

        @Test
        void 自販機の名前が変わらないとき値が更新される() {
            Integer id = 1;
            String name = "健康自販機";
            Integer slotCapacity = 10;
            Status status = Status.CLOSED;
            VendingMachineInput vmInput = new VendingMachineInput(name, slotCapacity, status);
            adminVendingMachineService.update(id, vmInput);
            VendingMachine vm = vendingMachineRepository.findById(id).get();
            assertEquals(name, vm.getName().value());
            assertEquals(slotCapacity, vm.getSlotCapacity().value());
            assertEquals(status, vm.getStatus());
        }
    }

    @Nested
    class deleteByIdメソッドのテスト {
        @Test
        void 自販機のidが存在しないとき例外が発生する() {
            Integer id = 99;
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> adminVendingMachineService.deleteById(id));
            assertEquals("指定された自販機は存在しません。", exception.getMessage());
        }

        @Test
        void 自販機のidが存在するとき削除される() {
            Integer id = 1;
            adminVendingMachineService.deleteById(id);
            Optional<VendingMachine> vm = vendingMachineRepository.findById(id);
            assertTrue(vm.isEmpty());
            Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM drink", Integer.class);
            assertEquals(4, count);
        }
    }
}
