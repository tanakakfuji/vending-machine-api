package com.github.tanakakfuji.vending_machine_api.domain.model.drink;

import org.springframework.data.relational.core.mapping.Column;

public record Stock(@Column("STOCK") Integer value) {
    public Stock {
        if (value == null) {
            throw new IllegalArgumentException("飲み物の在庫数を入力してください。");
        } else if (value < 0) {
            throw new IllegalArgumentException("飲み物の在庫数は0以上でなければいけません。");
        } else if (value > 1000) {
            throw new IllegalArgumentException("飲み物の在庫数は1000以下でなければいけません。");
        }
    }
}
