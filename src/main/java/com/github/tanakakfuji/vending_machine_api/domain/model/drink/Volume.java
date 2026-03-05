package com.github.tanakakfuji.vending_machine_api.domain.model.drink;

import org.springframework.data.relational.core.mapping.Column;

public record Volume(@Column("VOLUME") Integer value) {
    public Volume {
        if (value == null) {
            throw new IllegalArgumentException("飲み物の内容量を入力してください。");
        } else if (value <= 0) {
            throw new IllegalArgumentException("飲み物の内容量は1ml以上でなければいけません。");
        } else if (value > 10000) {
            throw new IllegalArgumentException("飲み物の内容量は10000ml以下でなければいけません。");
        }
    }
}
