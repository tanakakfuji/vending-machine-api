package com.github.tanakakfuji.vending_machine_api.domain.model.vm;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SlotCapacityTest {
    @Nested
    class コンストラクタのテスト {
        @Test
        void 値がnullのとき例外が発生する() {
            Integer value = null;
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new SlotCapacity(value));
            assertEquals("自販機のスロット数を入力してください。", exception.getMessage());
        }

        @ParameterizedTest
        @ValueSource(ints = {-100, -1})
        void 値が0より小さいとき例外が発生する(Integer value) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new SlotCapacity(value));
            assertEquals("自販機のスロット数は0以上でなければいけません。", exception.getMessage());
        }

        @ParameterizedTest
        @ValueSource(ints = {101, 150})
        void 値が100より大きいとき例外が発生する(Integer value) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new SlotCapacity(value));
            assertEquals("自販機のスロット数は100以下でなければいけません。", exception.getMessage());
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 50, 100})
        void 値が0以上1000以下のときフィールドが初期化される(Integer value) {
            SlotCapacity stock = new SlotCapacity(value);
            assertEquals(value, stock.value());
        }
    }
}
