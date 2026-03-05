package com.github.tanakakfuji.vending_machine_api.domain.model.drink;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NameTest {
    @Nested
    class コンストラクタのテスト {
        @ParameterizedTest
        @ValueSource(strings = {"", " "})
        @NullSource
        void 値が空のとき例外が発生する(String value) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Name(value));
            assertEquals("飲み物の名前を空にすることはできません", exception.getMessage());
        }

        @Test
        void 値が100文字より大きいとき例外が発生する() {
            String value = "a".repeat(200);
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Name(value));
            assertEquals("飲み物の名前は100文字以内である必要があります。", exception.getMessage());
        }

        @Test
        void 値が101文字のとき例外が発生する() {
            String value = "a".repeat(101);
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Name(value));
            assertEquals("飲み物の名前は100文字以内である必要があります。", exception.getMessage());
        }

        @Test
        void 値が100文字のときフィールドが初期化される() {
            String value = "a".repeat(100);
            Name name = new Name(value);
            assertEquals(value, name.value());
        }

        @Test
        void 値が100文字より小さいときフィールドが初期化される() {
            String value = "a".repeat(50);
            Name name = new Name(value);
            assertEquals(value, name.value());
        }

        @Test
        void 値が1文字のときフィールドが初期化される() {
            String value = "a".repeat(1);
            Name name = new Name(value);
            assertEquals(value, name.value());
        }
    }
}
