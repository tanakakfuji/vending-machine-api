package com.github.tanakakfuji.vending_machine_api.domain.model.vm;

import org.springframework.data.relational.core.mapping.Column;

public record SlotCapacity(@Column("SLOT_CAPACITY") Integer value) {
    public SlotCapacity {
        if (value == null) {
            throw new IllegalArgumentException("自販機のスロット数を入力してください。");
        } else if (value < 0) {
            throw new IllegalArgumentException("自販機のスロット数は0以上でなければいけません。");
        } else if (value > 100) {
            throw new IllegalArgumentException("自販機のスロット数は100以下でなければいけません。");
        }
    }
}
