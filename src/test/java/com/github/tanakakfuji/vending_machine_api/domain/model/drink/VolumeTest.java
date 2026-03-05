package com.github.tanakakfuji.vending_machine_api.domain.model.drink;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class VolumeTest {
    @Nested
    class コンストラクタのテスト {
        @Test
        void 値がnullのとき例外が発生する() {
            Integer value = null;
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Volume(value));
            assertEquals("飲み物の内容量を入力してください。", exception.getMessage());
        }

        @ParameterizedTest
        @ValueSource(ints = {-100, -1, 0})
        void 値が0以下のとき例外が発生する(Integer value) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Volume(value));
            assertEquals("飲み物の内容量は1ml以上でなければいけません。", exception.getMessage());
        }

        @ParameterizedTest
        @ValueSource(ints = {10001, 15000})
        void 値が10000より大きいとき例外が発生する(Integer value) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Volume(value));
            assertEquals("飲み物の内容量は10000ml以下でなければいけません。", exception.getMessage());
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 5000, 10000})
        void 値が1以上10000以下のときフィールドが初期化される(Integer value) {
            Volume volume = new Volume(value);
            assertEquals(value, volume.value());
        }
    }
}
