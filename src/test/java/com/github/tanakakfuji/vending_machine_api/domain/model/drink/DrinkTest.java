package com.github.tanakakfuji.vending_machine_api.domain.model.drink;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class DrinkTest {
    @Nested
    class createメソッドのテスト {
        @Test
        void vmIdがnullのとき例外が発生する() {
            Integer vmId = null;
            Name name = new Name("サンプル");
            Volume volume = new Volume(500);
            Price price = new Price(100);
            Stock stock = new Stock(0);
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Drink.create(vmId, name, volume, price, stock));
            assertEquals("自販機を指定してください。", exception.getMessage());
        }

        @Test
        void nameがnullのとき例外が発生する() {
            Integer vmId = 1;
            Name name = null;
            Volume volume = new Volume(500);
            Price price = new Price(100);
            Stock stock = new Stock(0);
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Drink.create(vmId, name, volume, price, stock));
            assertEquals("飲み物の名前を指定してください。", exception.getMessage());
        }

        @Test
        void volumeがnullのとき例外が発生する() {
            Integer vmId = 1;
            Name name = new Name("サンプル");
            Volume volume = null;
            Price price = new Price(100);
            Stock stock = new Stock(0);
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Drink.create(vmId, name, volume, price, stock));
            assertEquals("飲み物の内容量を指定してください。", exception.getMessage());
        }

        @Test
        void priceがnullのとき例外が発生する() {
            Integer vmId = 1;
            Name name = new Name("サンプル");
            Volume volume = new Volume(500);
            Price price = null;
            Stock stock = new Stock(0);
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Drink.create(vmId, name, volume, price, stock));
            assertEquals("飲み物の価格を指定してください。", exception.getMessage());
        }

        @Test
        void stockがnullのとき例外が発生する() {
            Integer vmId = 1;
            Name name = new Name("サンプル");
            Volume volume = new Volume(500);
            Price price = new Price(100);
            Stock stock = null;
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Drink.create(vmId, name, volume, price, stock));
            assertEquals("飲み物の在庫数を指定してください。", exception.getMessage());
        }

        @Test
        void 全ての引数がnullでないときフィールドが初期化される() {
            Integer vmId = 1;
            Name name = new Name("サンプル");
            Volume volume = new Volume(500);
            Price price = new Price(100);
            Stock stock = new Stock(0);
            Drink drink = Drink.create(vmId, name, volume, price, stock);
            assertNull(drink.getId());
            assertEquals(vmId, drink.getVmId());
            assertEquals(name, drink.getName());
            assertEquals(volume, drink.getVolume());
            assertEquals(price, drink.getPrice());
            assertEquals(stock, drink.getStock());
        }
    }

    @Nested
    class decrementStockメソッドのテスト {
        @Test
        void stockが0のとき例外が発生する() {
            Stock stock = new Stock(0);
            Drink drink = Drink.create(1, new Name("サンプル"), new Volume(500), new Price(100), stock);
            IllegalStateException exception = assertThrows(IllegalStateException.class, drink::decrementStock);
            assertEquals("飲み物の在庫数がありません。", exception.getMessage());
        }

        @Test
        void stockが0より大きいときstockが1減少する() {
            Stock stock = new Stock(1);
            Drink drink = Drink.create(1, new Name("サンプル"), new Volume(500), new Price(100), stock);
            drink.decrementStock();
            assertEquals(0, drink.getStock().value());
        }
    }

    @Nested
    class calculateChangeメソッドのテスト {
        @ParameterizedTest
        @ValueSource(ints = {-100, -1})
        void 投入金額が0より小さいとき例外が発生する(int money) {
            Drink drink = Drink.create(1, new Name("サンプル"), new Volume(500), new Price(100), new Stock(0));
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> drink.calculateChange(money));
            assertEquals("投入金額の値が不正です。", exception.getMessage());
        }

        @ParameterizedTest
        @ValueSource(ints = {100001, 150000})
        void 投入金額が100000より大きいとき例外が発生する(int money) {
            Drink drink = Drink.create(1, new Name("サンプル"), new Volume(500), new Price(100), new Stock(0));
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> drink.calculateChange(money));
            assertEquals("投入金額の値が不正です。", exception.getMessage());
        }

        @ParameterizedTest
        @ValueSource(ints = {50, 99})
        void 投入金額がpriceより小さいとき例外が発生する(int money) {
            Price price = new Price(100);
            Drink drink = Drink.create(1, new Name("サンプル"), new Volume(500), price, new Stock(0));
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> drink.calculateChange(money));
            assertEquals(String.format("投入金額が不足しています。飲み物の価格は%s円です。", price.value()), exception.getMessage());
        }

        @Test
        void 投入金額がpriceと等しいとき0が返される() {
            int money = 100;
            Price price = new Price(100);
            Drink drink = Drink.create(1, new Name("サンプル"), new Volume(500), price, new Stock(0));
            int actual = drink.calculateChange(money);
            assertEquals(0, actual);
        }

        @ParameterizedTest
        @ValueSource(ints = {101, 200})
        void 投入金額がpriceより大きいときお釣りが返される(int money) {
            Price price = new Price(100);
            Drink drink = Drink.create(1, new Name("サンプル"), new Volume(500), price, new Stock(0));
            int actual = drink.calculateChange(money);
            assertEquals(money - price.value(), actual);
        }
    }
}
