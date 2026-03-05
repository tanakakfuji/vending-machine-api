package com.github.tanakakfuji.vending_machine_api.domain.model.drink;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StockTest {
    @Nested
    class コンストラクタのテスト {
        @Test
        void 値がnullのとき例外が発生する() {
            Integer value = null;
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Stock(value));
            assertEquals("飲み物の在庫数を入力してください。", exception.getMessage());
        }

        @ParameterizedTest
        @ValueSource(ints = {-100, -1})
        void 値が0より小さいとき例外が発生する(Integer value) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Stock(value));
            assertEquals("飲み物の在庫数は0以上でなければいけません。", exception.getMessage());
        }

        @ParameterizedTest
        @ValueSource(ints = {1001, 1500})
        void 値が1000より大きいとき例外が発生する(Integer value) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Stock(value));
            assertEquals("飲み物の在庫数は1000以下でなければいけません。", exception.getMessage());
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 500, 1000})
        void 値が0以上1000以下のときフィールドが初期化される(Integer value) {
            Stock stock = new Stock(value);
            assertEquals(value, stock.value());
        }
    }
}
