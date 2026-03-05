package com.github.tanakakfuji.vending_machine_api.domain.model.drink;

import org.springframework.data.relational.core.mapping.Column;

public record Price(@Column("PRICE") Integer value) {
    public Price {
        if (value == null) {
            throw new IllegalArgumentException("飲み物の価格を入力してください。");
        } else if (value < 0) {
            throw new IllegalArgumentException("飲み物の価格は0以上でなければいけません。");
        } else if (value > 10000) {
            throw new IllegalArgumentException("飲み物の価格は10000以下でなければいけません。");
        }
    }
}
