package com.github.tanakakfuji.vending_machine_api.domain.model.drink;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;

@Getter
@ToString
@Table
public class Drink {
    @Id
    private final Integer id;
    private final Integer vmId;

    @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL)
    private final Name name;
    @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL)
    private final Volume volume;
    @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL)
    private final Price price;
    @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL)
    private Stock stock;

    private Drink(Integer id, Integer vmId, Name name, Volume volume, Price price, Stock stock) {
        if (vmId == null) throw new IllegalArgumentException("自販機を指定してください。");
        if (name == null) throw new IllegalArgumentException("飲み物の名前を指定してください。");
        if (volume == null) throw new IllegalArgumentException("飲み物の内容量を指定してください。");
        if (price == null) throw new IllegalArgumentException("飲み物の価格を指定してください。");
        if (stock == null) throw new IllegalArgumentException("飲み物の在庫数を指定してください。");
        this.id = id;
        this.vmId = vmId;
        this.name = name;
        this.volume = volume;
        this.price = price;
        this.stock = stock;
    }

    public static Drink create(Integer vmId, Name name, Volume volume, Price price, Stock stock) {
        return new Drink(null, vmId, name, volume, price, stock);
    }

    public void decrementStock() {
        if (stock.value() <= 0) {
            throw new IllegalStateException("飲み物の在庫数がありません。");
        }
        stock = new Stock(stock.value() - 1);
    }

    public int calculateChange(int money) {
        if (money < 0 || 100000 < money) throw new IllegalArgumentException("投入金額の値が不正です。");
        if (price.value() > money)
            throw new IllegalArgumentException(String.format("投入金額が不足しています。飲み物の価格は%s円です。", price.value()));
        return money - price.value();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Drink drink)) return false;
        if (id == null || drink.id == null) return false;
        return Objects.equals(id, drink.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
