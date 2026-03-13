package com.github.tanakakfuji.vending_machine_api.domain.model.vm;

import com.github.tanakakfuji.vending_machine_api.domain.model.drink.Drink;
import com.github.tanakakfuji.vending_machine_api.domain.model.drink.Price;
import com.github.tanakakfuji.vending_machine_api.domain.model.drink.Stock;
import com.github.tanakakfuji.vending_machine_api.domain.model.drink.Volume;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class VendingMachineTest {
    @Nested
    class createメソッドのテスト {
        @Test
        void nameがnullのとき例外が発生する() {
            Name name = null;
            SlotCapacity slotCapacity = new SlotCapacity(10);
            Status status = Status.OPEN;
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> VendingMachine.create(name, slotCapacity, status));
            assertEquals("自販機の名前を指定してください。", exception.getMessage());
        }

        @Test
        void slotCapacityがnullのとき例外が発生する() {
            Name name = new Name("サンプル");
            SlotCapacity slotCapacity = null;
            Status status = Status.OPEN;
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> VendingMachine.create(name, slotCapacity, status));
            assertEquals("自販機のスロット数を指定してください。", exception.getMessage());
        }

        @Test
        void statusがnullのとき例外が発生する() {
            Name name = new Name("サンプル");
            SlotCapacity slotCapacity = new SlotCapacity(10);
            Status status = null;
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> VendingMachine.create(name, slotCapacity, status));
            assertEquals("自販機のステータスを指定してください。", exception.getMessage());
        }

        @Test
        void 全ての引数がnullでないときフィールドが初期化される() {
            Name name = new Name("サンプル");
            SlotCapacity slotCapacity = new SlotCapacity(10);
            Status status = Status.OPEN;
            VendingMachine vendingMachine = VendingMachine.create(name, slotCapacity, status);
            assertEquals(name, vendingMachine.getName());
            assertEquals(slotCapacity, vendingMachine.getSlotCapacity());
            assertEquals(status, vendingMachine.getStatus());
        }
    }

    @Nested
    class updateメソッドのテスト {
        @Test
        void nameがnullのとき例外が発生する() {
            VendingMachine vendingMachine = VendingMachine.create(new Name("サンプル"), new SlotCapacity(10), Status.OPEN);
            Name name = null;
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> vendingMachine.update(name, new SlotCapacity(5), Status.CLOSED, new HashSet<>()));
            assertEquals("自販機の名前を指定してください。", exception.getMessage());
        }

        @Test
        void slotCapacityがnullのとき例外が発生する() {
            VendingMachine vendingMachine = VendingMachine.create(new Name("サンプル"), new SlotCapacity(10), Status.OPEN);
            SlotCapacity slotCapacity = null;
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> vendingMachine.update(new Name("更新後の名前"), slotCapacity, Status.CLOSED, new HashSet<>()));
            assertEquals("自販機のスロット数を指定してください。", exception.getMessage());
        }

        @Test
        void statusがnullのとき例外が発生する() {
            VendingMachine vendingMachine = VendingMachine.create(new Name("サンプル"), new SlotCapacity(10), Status.OPEN);
            Status status = null;
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> vendingMachine.update(new Name("更新後の名前"), new SlotCapacity(5), status, new HashSet<>()));
            assertEquals("自販機のステータスを指定してください。", exception.getMessage());
        }

        @Test
        void drinksがnullのとき例外が発生する() {
            VendingMachine vendingMachine = VendingMachine.create(new Name("サンプル"), new SlotCapacity(10), Status.OPEN);
            Set<Drink> drinks = null;
            NullPointerException exception = assertThrows(NullPointerException.class, () -> vendingMachine.update(new Name("更新後の名前"), new SlotCapacity(5), Status.CLOSED, drinks));
            assertEquals("drinksにnullを代入できません。", exception.getMessage());
        }

        @Test
        void 全ての引数がnullでなくdrinksが空のときフィールドが更新される() {
            VendingMachine vendingMachine = VendingMachine.create(new Name("サンプル"), new SlotCapacity(10), Status.OPEN);
            Name name = new Name("更新後の名前");
            SlotCapacity slotCapacity = new SlotCapacity(5);
            Status status = Status.CLOSED;
            Set<Drink> drinks = new HashSet<>();
            vendingMachine.update(name, slotCapacity, status, drinks);
            assertEquals(name, vendingMachine.getName());
            assertEquals(slotCapacity, vendingMachine.getSlotCapacity());
            assertEquals(status, vendingMachine.getStatus());
            assertEquals(drinks, vendingMachine.getDrinks());
        }

        @Test
        void drinkのvmIdが自販機のidと一致しないとき例外が発生する() {
            VendingMachine vendingMachine = VendingMachine.reconstruct(1, new Name("サンプル"), new SlotCapacity(10), Status.OPEN, new HashSet<>());
            Set<Drink> drinks = new HashSet<>();
            Integer vmId = 2;
            drinks.add(Drink.create(vmId, new com.github.tanakakfuji.vending_machine_api.domain.model.drink.Name("飲み物サンプル"), new Volume(500), new Price(100), new Stock(5)));
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> vendingMachine.update(new Name("更新後の名前"), new SlotCapacity(5), Status.CLOSED, drinks));
            assertEquals("対象の自販機が正しく指定されていません。", exception.getMessage());
        }

        @Test
        void 一部のdrinkのvmIdが自販機のidと一致しないとき例外が発生する() {
            VendingMachine vendingMachine = VendingMachine.reconstruct(1, new Name("サンプル"), new SlotCapacity(10), Status.OPEN, new HashSet<>());
            Set<Drink> drinks = new HashSet<>();
            drinks.add(Drink.create(1, new com.github.tanakakfuji.vending_machine_api.domain.model.drink.Name("飲み物サンプル1"), new Volume(500), new Price(100), new Stock(5)));
            drinks.add(Drink.create(2, new com.github.tanakakfuji.vending_machine_api.domain.model.drink.Name("飲み物サンプル2"), new Volume(500), new Price(100), new Stock(5)));
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> vendingMachine.update(new Name("更新後の名前"), new SlotCapacity(5), Status.CLOSED, drinks));
            assertEquals("対象の自販機が正しく指定されていません。", exception.getMessage());
        }

        @Test
        void drinkの名前が重複しているとき例外が発生する() {
            VendingMachine vendingMachine = VendingMachine.reconstruct(1, new Name("サンプル"), new SlotCapacity(10), Status.OPEN, new HashSet<>());
            Set<Drink> drinks = new HashSet<>();
            drinks.add(Drink.create(1, new com.github.tanakakfuji.vending_machine_api.domain.model.drink.Name("飲み物サンプル"), new Volume(500), new Price(100), new Stock(5)));
            drinks.add(Drink.create(1, new com.github.tanakakfuji.vending_machine_api.domain.model.drink.Name("飲み物サンプル"), new Volume(500), new Price(100), new Stock(5)));
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> vendingMachine.update(new Name("更新後の名前"), new SlotCapacity(5), Status.CLOSED, drinks));
            assertEquals("入力された飲み物の間で名前が重複しています。重複しない名前を入力してください。", exception.getMessage());
        }

        @Test
        void 全ての引数がnullでなくdrinksの中身が適切なときフィールドが更新される() {
            VendingMachine vendingMachine = VendingMachine.reconstruct(1, new Name("サンプル"), new SlotCapacity(10), Status.OPEN, new HashSet<>());
            Name name = new Name("更新後の名前");
            SlotCapacity slotCapacity = new SlotCapacity(5);
            Status status = Status.CLOSED;
            Set<Drink> drinks = new HashSet<>();
            drinks.add(Drink.create(1, new com.github.tanakakfuji.vending_machine_api.domain.model.drink.Name("飲み物サンプル1"), new Volume(500), new Price(100), new Stock(5)));
            drinks.add(Drink.create(1, new com.github.tanakakfuji.vending_machine_api.domain.model.drink.Name("飲み物サンプル2"), new Volume(500), new Price(100), new Stock(5)));
            vendingMachine.update(name, slotCapacity, status, drinks);
            assertEquals(name, vendingMachine.getName());
            assertEquals(slotCapacity, vendingMachine.getSlotCapacity());
            assertEquals(status, vendingMachine.getStatus());
            assertEquals(drinks, vendingMachine.getDrinks());
        }
    }

    @Nested
    class checkOpenedメソッドのテスト {
        @Test
        void statusがclosedのとき例外が発生する() {
            VendingMachine vendingMachine = VendingMachine.create(new Name("サンプル"), new SlotCapacity(10), Status.CLOSED);
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, vendingMachine::checkOpened);
            assertEquals("指定された自販機は現在公開されていません。", exception.getMessage());
        }

        @Test
        void statusがopenのとき例外が発生しない() {
            VendingMachine vendingMachine = VendingMachine.create(new Name("サンプル"), new SlotCapacity(10), Status.OPEN);
            assertDoesNotThrow(vendingMachine::checkOpened);
        }
    }

    @Nested
    class addDrinksメソッドのテスト {
        @Test
        void 追加する飲み物がnullのとき例外が発生する() {
            VendingMachine vendingMachine = VendingMachine.reconstruct(1, new Name("サンプル"), new SlotCapacity(0), Status.OPEN, new HashSet<>());
            Set<Drink> drinks = null;
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> vendingMachine.addDrinks(drinks));
            assertEquals("自販機に追加する飲み物を指定してください。", exception.getMessage());
        }

        @Test
        void 追加する飲み物が空のとき例外が発生する() {
            VendingMachine vendingMachine = VendingMachine.reconstruct(1, new Name("サンプル"), new SlotCapacity(0), Status.OPEN, new HashSet<>());
            Set<Drink> drinks = new HashSet<>();
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> vendingMachine.addDrinks(drinks));
            assertEquals("自販機に追加する飲み物を指定してください。", exception.getMessage());
        }

        @Test
        void 自販機のidがnullのとき例外が発生する() {
            VendingMachine vendingMachine = VendingMachine.reconstruct(null, new Name("サンプル"), new SlotCapacity(0), Status.OPEN, new HashSet<>());
            Drink drink = Drink.create(1, new com.github.tanakakfuji.vending_machine_api.domain.model.drink.Name("サンプル1"), new Volume(500), new Price(100), new Stock(0));
            IllegalStateException exception = assertThrows(IllegalStateException.class, () -> vendingMachine.addDrinks(Set.of(drink)));
            assertEquals("自販機を登録した後に飲み物を追加する必要があります。", exception.getMessage());
        }

        @Test
        void スロット数が0のとき例外が発生する() {
            VendingMachine vendingMachine = VendingMachine.reconstruct(1, new Name("サンプル"), new SlotCapacity(0), Status.OPEN, new HashSet<>());
            Set<Drink> drinks = Set.of(Drink.create(1, new com.github.tanakakfuji.vending_machine_api.domain.model.drink.Name("サンプル"), new Volume(500), new Price(100), new Stock(0)));
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> vendingMachine.addDrinks(drinks));
            assertEquals("自販機のスロット数を超えて飲み物を追加できません。", exception.getMessage());
        }

        @Test
        void 自販機に追加する飲み物の数がスロット数より多いとき例外が発生する() {
            VendingMachine vendingMachine = VendingMachine.reconstruct(1, new Name("サンプル"), new SlotCapacity(2), Status.OPEN, new HashSet<>());
            Set<Drink> drinks = Set.of(
                    Drink.create(1, new com.github.tanakakfuji.vending_machine_api.domain.model.drink.Name("サンプル1"), new Volume(500), new Price(100), new Stock(0)),
                    Drink.create(1, new com.github.tanakakfuji.vending_machine_api.domain.model.drink.Name("サンプル2"), new Volume(500), new Price(100), new Stock(0)),
                    Drink.create(1, new com.github.tanakakfuji.vending_machine_api.domain.model.drink.Name("サンプル3"), new Volume(500), new Price(100), new Stock(0))
            );
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> vendingMachine.addDrinks(drinks));
            assertEquals("自販機のスロット数を超えて飲み物を追加できません。", exception.getMessage());
        }

        @Test
        void 自販機に追加する飲み物の数が既存の数と合わせてスロット数より多いとき例外が発生する() {
            VendingMachine vendingMachine = VendingMachine.reconstruct(1, new Name("サンプル"), new SlotCapacity(2), Status.OPEN, new HashSet<>());
            Drink drink1 = Drink.create(1, new com.github.tanakakfuji.vending_machine_api.domain.model.drink.Name("サンプル1"), new Volume(500), new Price(100), new Stock(0));
            vendingMachine.addDrinks(Set.of(drink1));
            Drink drink2 = Drink.create(1, new com.github.tanakakfuji.vending_machine_api.domain.model.drink.Name("サンプル2"), new Volume(500), new Price(100), new Stock(0));
            Drink drink3 = Drink.create(1, new com.github.tanakakfuji.vending_machine_api.domain.model.drink.Name("サンプル3"), new Volume(500), new Price(100), new Stock(0));
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> vendingMachine.addDrinks(Set.of(drink2, drink3)));
            assertEquals("自販機のスロット数を超えて飲み物を追加できません。", exception.getMessage());
        }

        @Test
        void 自販機に追加する飲み物の数がスロット数と同じとき飲み物が追加される() {
            VendingMachine vendingMachine = VendingMachine.reconstruct(1, new Name("サンプル"), new SlotCapacity(2), Status.OPEN, new HashSet<>());
            Drink drink1 = Drink.create(1, new com.github.tanakakfuji.vending_machine_api.domain.model.drink.Name("サンプル1"), new Volume(500), new Price(100), new Stock(0));
            Drink drink2 = Drink.create(1, new com.github.tanakakfuji.vending_machine_api.domain.model.drink.Name("サンプル2"), new Volume(500), new Price(100), new Stock(0));
            Set<Drink> drinks = Set.of(drink1, drink2);
            vendingMachine.addDrinks(drinks);
            assertEquals(drinks, vendingMachine.getDrinks());
        }

        @Test
        void 自販機に追加する飲み物の数が既存の数と合わせてスロット数と同じとき飲み物が追加される() {
            VendingMachine vendingMachine = VendingMachine.reconstruct(1, new Name("サンプル"), new SlotCapacity(2), Status.OPEN, new HashSet<>());
            Drink drink1 = Drink.create(1, new com.github.tanakakfuji.vending_machine_api.domain.model.drink.Name("サンプル1"), new Volume(500), new Price(100), new Stock(0));
            vendingMachine.addDrinks(Set.of(drink1));
            Drink drink2 = Drink.create(1, new com.github.tanakakfuji.vending_machine_api.domain.model.drink.Name("サンプル2"), new Volume(500), new Price(100), new Stock(0));
            vendingMachine.addDrinks(Set.of(drink2));
            Set<Drink> drinks = Set.of(drink1, drink2);
            assertEquals(drinks, vendingMachine.getDrinks());
        }

        @Test
        void 自販機に追加する飲み物の数がスロット数より少ないとき飲み物が追加される() {
            VendingMachine vendingMachine = VendingMachine.reconstruct(1, new Name("サンプル"), new SlotCapacity(3), Status.OPEN, new HashSet<>());
            Drink drink1 = Drink.create(1, new com.github.tanakakfuji.vending_machine_api.domain.model.drink.Name("サンプル1"), new Volume(500), new Price(100), new Stock(0));
            Drink drink2 = Drink.create(1, new com.github.tanakakfuji.vending_machine_api.domain.model.drink.Name("サンプル2"), new Volume(500), new Price(100), new Stock(0));
            Set<Drink> drinks = Set.of(drink1, drink2);
            vendingMachine.addDrinks(drinks);
            assertEquals(drinks, vendingMachine.getDrinks());
        }

        @Test
        void 自販機に追加する飲み物の数が既存の数と合わせてスロット数より少ないとき飲み物が追加される() {
            VendingMachine vendingMachine = VendingMachine.reconstruct(1, new Name("サンプル"), new SlotCapacity(3), Status.OPEN, new HashSet<>());
            Drink drink1 = Drink.create(1, new com.github.tanakakfuji.vending_machine_api.domain.model.drink.Name("サンプル1"), new Volume(500), new Price(100), new Stock(0));
            vendingMachine.addDrinks(Set.of(drink1));
            Drink drink2 = Drink.create(1, new com.github.tanakakfuji.vending_machine_api.domain.model.drink.Name("サンプル2"), new Volume(500), new Price(100), new Stock(0));
            vendingMachine.addDrinks(Set.of(drink2));
            Set<Drink> drinks = Set.of(drink1, drink2);
            assertEquals(drinks, vendingMachine.getDrinks());
        }

        @Test
        void 自販機のidが一致しないとき例外が発生する() {
            VendingMachine vendingMachine = VendingMachine.reconstruct(1, new Name("サンプル"), new SlotCapacity(5), Status.OPEN, new HashSet<>());
            Drink drink = Drink.create(2, new com.github.tanakakfuji.vending_machine_api.domain.model.drink.Name("サンプル1"), new Volume(500), new Price(100), new Stock(0));
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> vendingMachine.addDrinks(Set.of(drink)));
            assertEquals("対象の自販機が正しく指定されていません。", exception.getMessage());
        }

        @Test
        void 一部の飲み物で自販機のidが一致しないとき例外が発生する() {
            VendingMachine vendingMachine = VendingMachine.reconstruct(1, new Name("サンプル"), new SlotCapacity(5), Status.OPEN, new HashSet<>());
            Drink drink1 = Drink.create(1, new com.github.tanakakfuji.vending_machine_api.domain.model.drink.Name("サンプル1"), new Volume(500), new Price(100), new Stock(0));
            Drink drink2 = Drink.create(2, new com.github.tanakakfuji.vending_machine_api.domain.model.drink.Name("サンプル2"), new Volume(500), new Price(100), new Stock(0));
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> vendingMachine.addDrinks(Set.of(drink1, drink2)));
            assertEquals("対象の自販機が正しく指定されていません。", exception.getMessage());
        }

        @Test
        void 自販機に追加する飲み物の名前が重複しているとき例外が発生する() {
            VendingMachine vendingMachine = VendingMachine.reconstruct(1, new Name("サンプル"), new SlotCapacity(3), Status.OPEN, new HashSet<>());
            Drink drink1 = Drink.create(1, new com.github.tanakakfuji.vending_machine_api.domain.model.drink.Name("サンプル1"), new Volume(500), new Price(100), new Stock(0));
            Drink drink2 = Drink.create(1, new com.github.tanakakfuji.vending_machine_api.domain.model.drink.Name("サンプル1"), new Volume(400), new Price(120), new Stock(5));
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> vendingMachine.addDrinks(Set.of(drink1, drink2)));
            assertEquals("入力された飲み物の間で名前が重複しています。重複しない名前を入力してください。", exception.getMessage());
        }

        @Test
        void 自販機に追加する飲み物の名前が既存の飲み物の名前と一致するとき例外が発生する() {
            VendingMachine vendingMachine = VendingMachine.reconstruct(1, new Name("サンプル"), new SlotCapacity(3), Status.OPEN, new HashSet<>());
            Drink drink1 = Drink.create(1, new com.github.tanakakfuji.vending_machine_api.domain.model.drink.Name("サンプル1"), new Volume(500), new Price(100), new Stock(0));
            vendingMachine.addDrinks(Set.of(drink1));
            Drink drink2 = Drink.create(1, new com.github.tanakakfuji.vending_machine_api.domain.model.drink.Name("サンプル1"), new Volume(400), new Price(120), new Stock(5));
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> vendingMachine.addDrinks(Set.of(drink2)));
            assertEquals("入力された飲み物の名前が既に存在します。重複しない名前を入力してください。", exception.getMessage());
        }

        @Test
        void 自販機に追加する一部の飲み物の名前が既存の飲み物の名前と一致するとき例外が発生する() {
            VendingMachine vendingMachine = VendingMachine.reconstruct(1, new Name("サンプル"), new SlotCapacity(3), Status.OPEN, new HashSet<>());
            Drink drink1 = Drink.create(1, new com.github.tanakakfuji.vending_machine_api.domain.model.drink.Name("サンプル1"), new Volume(500), new Price(100), new Stock(0));
            Drink drink2 = Drink.create(1, new com.github.tanakakfuji.vending_machine_api.domain.model.drink.Name("サンプル2"), new Volume(500), new Price(100), new Stock(0));
            vendingMachine.addDrinks(Set.of(drink1, drink2));
            Drink drink3 = Drink.create(1, new com.github.tanakakfuji.vending_machine_api.domain.model.drink.Name("サンプル2"), new Volume(400), new Price(120), new Stock(5));
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> vendingMachine.addDrinks(Set.of(drink3)));
            assertEquals("入力された飲み物の名前が既に存在します。重複しない名前を入力してください。", exception.getMessage());
        }

        @Test
        void 自販機に追加する飲み物の名前が重複しないとき飲み物が追加される() {
            VendingMachine vendingMachine = VendingMachine.reconstruct(1, new Name("サンプル"), new SlotCapacity(3), Status.OPEN, new HashSet<>());
            Drink drink1 = Drink.create(1, new com.github.tanakakfuji.vending_machine_api.domain.model.drink.Name("サンプル1"), new Volume(500), new Price(100), new Stock(0));
            Drink drink2 = Drink.create(1, new com.github.tanakakfuji.vending_machine_api.domain.model.drink.Name("サンプル2"), new Volume(500), new Price(100), new Stock(0));
            vendingMachine.addDrinks(Set.of(drink1, drink2));
            Drink drink3 = Drink.create(1, new com.github.tanakakfuji.vending_machine_api.domain.model.drink.Name("サンプル3"), new Volume(400), new Price(120), new Stock(5));
            vendingMachine.addDrinks(Set.of(drink3));
            assertEquals(Set.of(drink1, drink2, drink3), vendingMachine.getDrinks());
        }
    }
}
