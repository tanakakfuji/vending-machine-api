package com.github.tanakakfuji.vending_machine_api.domain.model.vm;

import org.springframework.data.relational.core.mapping.Column;

public record Name(@Column("NAME") String value) {
    public Name {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("自販機の名前を空にすることはできません");
        } else if (value.length() > 100) {
            throw new IllegalArgumentException("自販機の名前は100文字以内である必要があります。");
        }
    }
}