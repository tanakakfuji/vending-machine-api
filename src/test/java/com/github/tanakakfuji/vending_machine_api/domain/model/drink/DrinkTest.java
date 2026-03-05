package com.github.tanakakfuji.vending_machine_api.domain.model.drink;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

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
}
