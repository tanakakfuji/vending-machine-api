package com.github.tanakakfuji.vending_machine_api.service.vm;

import com.github.tanakakfuji.vending_machine_api.domain.model.drink.Drink;
import com.github.tanakakfuji.vending_machine_api.domain.model.vm.VendingMachine;
import com.github.tanakakfuji.vending_machine_api.input.vm.PaymentInput;
import com.github.tanakakfuji.vending_machine_api.repository.VendingMachineRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
@Sql("VendingMachineServiceIntegrationTest.sql")
public class VendingMachineServiceIntegrationTest {
    @Autowired
    VendingMachineService vendingMachineService;
    @Autowired
    VendingMachineRepository vendingMachineRepository;

    @Nested
    class findOpenVmsメソッドのテスト {
        @Test
        void ステータスがOPENの自販機が返される() {
            List<VendingMachine> vendingMachines = vendingMachineService.findOpenVms();
            assertEquals(2, vendingMachines.size());
            VendingMachine vm1 = vendingMachines.stream().filter(vm -> vm.getId() == 1).findFirst().get();
            Drink d1 = vm1.getDrinks().stream().filter(d -> d.getId() == 1).findFirst().get();
            Drink d2 = vm1.getDrinks().stream().filter(d -> d.getId() == 2).findFirst().get();
            assertEquals("健康自販機", vm1.getName().value());
            assertEquals("オレンジジュース", d1.getName().value());
            assertEquals("水", d2.getName().value());
            VendingMachine vm3 = vendingMachines.stream().filter(vm -> vm.getId() == 3).findFirst().get();
            Drink d5 = vm3.getDrinks().stream().filter(d -> d.getId() == 5).findFirst().get();
            Drink d6 = vm3.getDrinks().stream().filter(d -> d.getId() == 6).findFirst().get();
            assertEquals("甘党自販機", vendingMachines.get(1).getName().value());
            assertEquals("三ツ矢サイダー", d5.getName().value());
            assertEquals("コカ・コーラ", d6.getName().value());
        }

        @Test
        @Sql("VendingMachineServiceIntegrationTestAllClosed.sql")
        void 全ての自販機のステータスがCLOSEDのとき空のリストが返される() {
            List<VendingMachine> vendingMachines = vendingMachineService.findOpenVms();
            assertEquals(0, vendingMachines.size());
        }
    }

    @Nested
    class purchaseDrinkメソッドのテスト {
        @Test
        void 自販機のidがnullのとき例外が発生する() {
            Integer vmId = null;
            Integer drinkId = 1;
            PaymentInput paymentInput = new PaymentInput(500);
            NullPointerException exception = assertThrows(NullPointerException.class, () -> vendingMachineService.purchaseDrink(vmId, drinkId, paymentInput));
            assertEquals("自販機のidが不正な値です。", exception.getMessage());
        }

        @Test
        void 飲み物のidがnullのとき例外が発生する() {
            Integer vmId = 1;
            Integer drinkId = null;
            PaymentInput paymentInput = new PaymentInput(500);
            NullPointerException exception = assertThrows(NullPointerException.class, () -> vendingMachineService.purchaseDrink(vmId, drinkId, paymentInput));
            assertEquals("飲み物のidが不正な値です。", exception.getMessage());
        }

        @Test
        void 投入金額がnullのとき例外が発生する() {
            Integer vmId = 1;
            Integer drinkId = 1;
            PaymentInput paymentInput = null;
            NullPointerException exception = assertThrows(NullPointerException.class, () -> vendingMachineService.purchaseDrink(vmId, drinkId, paymentInput));
            assertEquals("投入金額が不正な値です。", exception.getMessage());
        }

        @Test
        void 自販機のidが存在しないとき例外が発生する() {
            Integer vmId = 10;
            Integer drinkId = 1;
            PaymentInput paymentInput = new PaymentInput(500);
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> vendingMachineService.purchaseDrink(vmId, drinkId, paymentInput));
            assertEquals("指定された自販機は存在しません。", exception.getMessage());
        }

        @Test
        void 自販機が非公開のとき例外が発生する() {
            Integer vmId = 2;
            Integer drinkId = 3;
            PaymentInput paymentInput = new PaymentInput(500);
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> vendingMachineService.purchaseDrink(vmId, drinkId, paymentInput));
            assertEquals("指定された自販機は現在公開されていません。", exception.getMessage());
        }

        @Test
        void 飲み物のidが存在しないとき例外が発生する() {
            Integer vmId = 1;
            Integer drinkId = 10;
            PaymentInput paymentInput = new PaymentInput(500);
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> vendingMachineService.purchaseDrink(vmId, drinkId, paymentInput));
            assertEquals("指定された飲み物は存在しません。", exception.getMessage());
        }

        @Test
        void 投入金額が不足しているとき例外が発生する() {
            Integer vmId = 1;
            Integer drinkId = 1;
            PaymentInput paymentInput = new PaymentInput(50);
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> vendingMachineService.purchaseDrink(vmId, drinkId, paymentInput));
            assertEquals("投入金額が不足しています。飲み物の価格は140円です。", exception.getMessage());
        }

        @Test
        void 引数が正しいときstockが1減少しお釣りが返される() {
            Integer vmId = 1;
            Integer drinkId = 1;
            PaymentInput paymentInput = new PaymentInput(500);
            int diff = vendingMachineService.purchaseDrink(vmId, drinkId, paymentInput);
            VendingMachine vm = vendingMachineRepository.findById(vmId).get();
            Drink drink = vm.getDrinks().stream().filter(d -> d.getId().equals(drinkId)).findFirst().get();
            assertEquals(360, diff);
            assertEquals(3, drink.getStock().value());
        }
    }
}
