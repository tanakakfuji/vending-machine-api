package com.github.tanakakfuji.vending_machine_api.service.drink;

import com.github.tanakakfuji.vending_machine_api.domain.model.drink.Drink;
import com.github.tanakakfuji.vending_machine_api.domain.model.vm.VendingMachine;
import com.github.tanakakfuji.vending_machine_api.input.drink.DrinkInput;
import com.github.tanakakfuji.vending_machine_api.input.drink.DrinkListInput;
import com.github.tanakakfuji.vending_machine_api.repository.VendingMachineRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
@Sql("AdminDrinkServiceIntegrationTest.sql")
public class AdminDrinkServiceIntegrationTest {
    @Autowired
    AdminDrinkService adminDrinkService;

    @Autowired
    VendingMachineRepository vendingMachineRepository;

    @Nested
    @Sql("AdminDrinkServiceIntegrationTestWithoutDrinkId.sql")
    class createメソッドのテスト {
        @Test
        void 自販機のidがnullのとき例外が発生する() {
            Integer vmId = null;
            DrinkListInput drinkListInput = new DrinkListInput(new ArrayList<>());
            NullPointerException exception = assertThrows(NullPointerException.class, () -> adminDrinkService.create(vmId, drinkListInput));
            assertEquals("自販機を指定してください。", exception.getMessage());
        }

        @Test
        void 登録飲み物がnullのとき例外が発生する() {
            Integer vmId = 1;
            DrinkListInput drinkListInput = null;
            NullPointerException exception = assertThrows(NullPointerException.class, () -> adminDrinkService.create(vmId, drinkListInput));
            assertEquals("登録飲み物が不正な値です。", exception.getMessage());
        }

        @Test
        void 指定された自販機が存在しないとき例外が発生する() {
            Integer vmId = 99;
            DrinkListInput drinkListInput = new DrinkListInput(new ArrayList<>());
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> adminDrinkService.create(vmId, drinkListInput));
            assertEquals("指定された自販機は存在しません。", exception.getMessage());
        }

        @Test
        void 登録飲み物が空のとき例外が発生する() {
            Integer vmId = 1;
            DrinkListInput drinkListInput = new DrinkListInput(new ArrayList<>());
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> adminDrinkService.create(vmId, drinkListInput));
            assertEquals("自販機に追加する飲み物を指定してください。", exception.getMessage());
        }

        @Test
        void 登録飲み物の間で同じものがあるとき例外が発生する() {
            Integer vmId = 1;
            List<DrinkInput> drinks = new ArrayList<>();
            drinks.add(new DrinkInput("重複する名前", 500, 100, 5));
            drinks.add(new DrinkInput("重複する名前", 500, 100, 5));
            DrinkListInput drinkListInput = new DrinkListInput(drinks);
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> adminDrinkService.create(vmId, drinkListInput));
            assertEquals("入力された飲み物の間で名前が重複しています。重複しない名前を入力してください。", exception.getMessage());
        }

        @Test
        void 登録飲み物の間で名前が重複するとき例外が発生する() {
            Integer vmId = 1;
            List<DrinkInput> drinks = new ArrayList<>();
            drinks.add(new DrinkInput("重複する名前", 500, 100, 5));
            drinks.add(new DrinkInput("重複する名前", 400, 200, 10));
            DrinkListInput drinkListInput = new DrinkListInput(drinks);
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> adminDrinkService.create(vmId, drinkListInput));
            assertEquals("入力された飲み物の間で名前が重複しています。重複しない名前を入力してください。", exception.getMessage());
        }

        @Test
        void 登録飲み物と自販機内の飲み物の名前が重複するとき例外が発生する() {
            Integer vmId = 1;
            List<DrinkInput> drinks = new ArrayList<>();
            drinks.add(new DrinkInput("オレンジジュース", 500, 100, 5));
            DrinkListInput drinkListInput = new DrinkListInput(drinks);
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> adminDrinkService.create(vmId, drinkListInput));
            assertEquals("入力された飲み物の名前が既に存在します。重複しない名前を入力してください。", exception.getMessage());
        }

        @Test
        void 自販機のidが存在し飲み物の名前が重複しないとき追加される() {
            Integer vmId = 1;
            List<DrinkInput> drinks = new ArrayList<>();
            drinks.add(new DrinkInput("重複しない名前1", 500, 100, 5));
            drinks.add(new DrinkInput("重複しない名前2", 500, 100, 5));
            DrinkListInput drinkListInput = new DrinkListInput(drinks);
            adminDrinkService.create(vmId, drinkListInput);
            VendingMachine vm = vendingMachineRepository.findById(vmId).get();
            Set<Drink> vmDrinks = vm.getDrinks();
            assertTrue(vmDrinks.stream().anyMatch(d -> d.getName().value().equals(drinks.get(0).name())));
            assertTrue(vmDrinks.stream().anyMatch(d -> d.getName().value().equals(drinks.get(1).name())));
        }
    }

    @Nested
    class updateメソッドのテスト {
        @Test
        void 自販機のidがnullのとき例外が発生する() {
            Integer vmId = null;
            Integer drinkId = 1;
            DrinkInput drinkInput = new DrinkInput("更新後の名前", 500, 100, 5);
            NullPointerException exception = assertThrows(NullPointerException.class, () -> adminDrinkService.update(vmId, drinkId, drinkInput));
            assertEquals("自販機を指定してください。", exception.getMessage());
        }

        @Test
        void 飲み物のidがnullのとき例外が発生する() {
            Integer vmId = 1;
            Integer drinkId = null;
            DrinkInput drinkInput = new DrinkInput("更新後の名前", 500, 100, 5);
            NullPointerException exception = assertThrows(NullPointerException.class, () -> adminDrinkService.update(vmId, drinkId, drinkInput));
            assertEquals("飲み物を指定してください。", exception.getMessage());
        }

        @Test
        void 登録飲み物がnullのとき例外が発生する() {
            Integer vmId = 1;
            Integer drinkId = 1;
            DrinkInput drinkInput = null;
            NullPointerException exception = assertThrows(NullPointerException.class, () -> adminDrinkService.update(vmId, drinkId, drinkInput));
            assertEquals("登録飲み物が不正な値です。", exception.getMessage());
        }

        @Test
        void 指定された自販機が存在しないとき例外が発生する() {
            Integer vmId = 99;
            Integer drinkId = 1;
            DrinkInput drinkInput = new DrinkInput("更新後の名前", 500, 100, 5);
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> adminDrinkService.update(vmId, drinkId, drinkInput));
            assertEquals("指定された自販機は存在しません。", exception.getMessage());
        }

        @Test
        void 指定された自販機が飲み物を持たないとき例外が発生する() {
            Integer vmId = 4;
            Integer drinkId = 1;
            DrinkInput drinkInput = new DrinkInput("更新後の名前", 500, 100, 5);
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> adminDrinkService.update(vmId, drinkId, drinkInput));
            assertEquals("指定された飲み物は存在しません。", exception.getMessage());
        }

        @Test
        void 指定された飲み物が存在しないとき例外が発生する() {
            Integer vmId = 1;
            Integer drinkId = 99;
            DrinkInput drinkInput = new DrinkInput("更新後の名前", 500, 100, 5);
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> adminDrinkService.update(vmId, drinkId, drinkInput));
            assertEquals("指定された飲み物は存在しません。", exception.getMessage());
        }

        @Test
        void 指定された自販機内に飲み物が存在しないとき例外が発生する() {
            Integer vmId = 1;
            Integer drinkId = 3;
            DrinkInput drinkInput = new DrinkInput("更新後の名前", 500, 100, 5);
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> adminDrinkService.update(vmId, drinkId, drinkInput));
            assertEquals("指定された飲み物は存在しません。", exception.getMessage());
        }

        @Test
        void 指定された自販機内に異なるidで同じ名前の飲み物があるとき例外が発生する() {
            Integer vmId = 1;
            Integer drinkId = 1;
            DrinkInput drinkInput = new DrinkInput("水", 500, 100, 5);
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> adminDrinkService.update(vmId, drinkId, drinkInput));
            assertEquals("自販機内で飲み物の名前が重複します。", exception.getMessage());
        }

        @Test
        void 指定された飲み物の名前が変わらないとき値が更新される() {
            Integer vmId = 1;
            Integer drinkId = 1;
            String name = "オレンジジュース";
            Integer volume = 500;
            Integer price = 100;
            Integer stock = 5;
            DrinkInput drinkInput = new DrinkInput(name, volume, price, stock);
            adminDrinkService.update(vmId, drinkId, drinkInput);
            VendingMachine vm = vendingMachineRepository.findById(vmId).get();
            Set<Drink> vmDrinks = vm.getDrinks();
            Drink drink = vmDrinks.stream().filter(d -> d.getId().equals(drinkId)).findFirst().get();
            assertEquals(name, drink.getName().value());
            assertEquals(volume, drink.getVolume().value());
            assertEquals(price, drink.getPrice().value());
            assertEquals(stock, drink.getStock().value());
        }

        @Test
        void 指定された飲み物の名前が重複しないとき値が更新される() {
            Integer vmId = 1;
            Integer drinkId = 1;
            String name = "更新後の名前";
            Integer volume = 500;
            Integer price = 100;
            Integer stock = 5;
            DrinkInput drinkInput = new DrinkInput(name, volume, price, stock);
            adminDrinkService.update(vmId, drinkId, drinkInput);
            VendingMachine vm = vendingMachineRepository.findById(vmId).get();
            Set<Drink> vmDrinks = vm.getDrinks();
            Drink drink = vmDrinks.stream().filter(d -> d.getId().equals(drinkId)).findFirst().get();
            assertEquals(name, drink.getName().value());
            assertEquals(volume, drink.getVolume().value());
            assertEquals(price, drink.getPrice().value());
            assertEquals(stock, drink.getStock().value());
        }
    }

    @Nested
    class deleteByIdメソッドのテスト {
        @Test
        void 自販機のidがnullのとき例外が発生する() {
            Integer vmId = null;
            Integer drinkId = 1;
            NullPointerException exception = assertThrows(NullPointerException.class, () -> adminDrinkService.deleteById(vmId, drinkId));
            assertEquals("自販機を指定してください。", exception.getMessage());
        }

        @Test
        void 飲み物のidがnullのとき例外が発生する() {
            Integer vmId = 1;
            Integer drinkId = null;
            NullPointerException exception = assertThrows(NullPointerException.class, () -> adminDrinkService.deleteById(vmId, drinkId));
            assertEquals("飲み物を指定してください。", exception.getMessage());
        }

        @Test
        void 指定された自販機が存在しないとき例外が発生する() {
            Integer vmId = 99;
            Integer drinkId = 1;
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> adminDrinkService.deleteById(vmId, drinkId));
            assertEquals("指定された自販機は存在しません。", exception.getMessage());
        }

        @Test
        void 指定された飲み物が存在しないとき例外が発生する() {
            Integer vmId = 1;
            Integer drinkId = 99;
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> adminDrinkService.deleteById(vmId, drinkId));
            assertEquals("指定された飲み物は存在しません。", exception.getMessage());
        }

        @Test
        void 指定された自販機が飲み物を持たないとき例外が発生する() {
            Integer vmId = 4;
            Integer drinkId = 1;
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> adminDrinkService.deleteById(vmId, drinkId));
            assertEquals("指定された飲み物は存在しません。", exception.getMessage());
        }

        @Test
        void 指定された自販機内に飲み物が存在しないとき例外が発生する() {
            Integer vmId = 1;
            Integer drinkId = 3;
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> adminDrinkService.deleteById(vmId, drinkId));
            assertEquals("指定された飲み物は存在しません。", exception.getMessage());
        }

        @Test
        void 指定された自販機内に飲み物が存在するとき削除される() {
            Integer vmId = 1;
            Integer drinkId = 1;
            adminDrinkService.deleteById(vmId, drinkId);
            VendingMachine vm = vendingMachineRepository.findById(vmId).get();
            Integer count = vm.getDrinks().size();
            assertEquals(1, count);
        }
    }
}
