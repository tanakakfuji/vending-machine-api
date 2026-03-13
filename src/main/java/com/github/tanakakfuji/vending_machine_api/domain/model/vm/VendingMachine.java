package com.github.tanakakfuji.vending_machine_api.domain.model.vm;

import com.github.tanakakfuji.vending_machine_api.domain.model.drink.Drink;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@ToString
@Table("VENDING_MACHINE")
public class VendingMachine {
    @Id
    private final Integer id;

    @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL)
    private final Name name;
    @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL)
    private final SlotCapacity slotCapacity;
    private final Status status;
    @MappedCollection(idColumn = "VM_ID")
    private final Set<Drink> drinks;

    private VendingMachine(Integer id, Name name, SlotCapacity slotCapacity, Status status, Set<Drink> drinks) {
        if (name == null) throw new IllegalArgumentException("自販機の名前を指定してください。");
        if (slotCapacity == null) throw new IllegalArgumentException("自販機のスロット数を指定してください。");
        if (status == null) throw new IllegalArgumentException("自販機のステータスを指定してください。");
        if (drinks == null) throw new NullPointerException("drinksにnullを代入できません。");
        this.id = id;
        this.name = name;
        this.slotCapacity = slotCapacity;
        this.status = status;
        this.drinks = drinks;
    }

    public static VendingMachine create(Name name, SlotCapacity slotCapacity, Status status) {
        return new VendingMachine(null, name, slotCapacity, status, new HashSet<>());
    }

    public static VendingMachine reconstruct(Integer id, Name name, SlotCapacity slotCapacity, Status status, Set<Drink> drinks) {
        return new VendingMachine(id, name, slotCapacity, status, drinks);
    }

    public void checkOpened() {
        if (status == Status.CLOSED) throw new IllegalArgumentException("指定された自販機は現在公開されていません。");
    }

    public void addDrinks(Set<Drink> drinkSet) {
        if (drinkSet == null || drinkSet.isEmpty()) {
            throw new IllegalArgumentException("自販機に追加する飲み物を指定してください。");
        }
        if (id == null) {
            throw new IllegalStateException("自販機を登録した後に飲み物を追加する必要があります。");
        }
        if (!isValidVmId(drinkSet)) {
            throw new IllegalArgumentException("対象の自販機が正しく指定されていません。");
        }
        if (drinks.size() + drinkSet.size() > slotCapacity.value()) {
            throw new IllegalArgumentException("自販機のスロット数を超えて飲み物を追加できません。");
        }
        if (hasDuplicateName(drinkSet)) {
            throw new DataDuplicateException("自販機内で飲み物の名前が重複します。");
        }
        drinks.addAll(drinkSet);
    }

    private void checkAdditionalDuplicateName(Set<Drink> drinkSet) {
        checkInputDuplicateName(drinkSet);
        Set<String> existingNames = drinks.stream().map(d -> d.getName().value()).collect(Collectors.toSet());
        if (drinkSet.stream().anyMatch(d -> existingNames.contains(d.getName().value())))
            throw new IllegalArgumentException("入力された飲み物の名前が既に存在します。重複しない名前を入力してください。");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VendingMachine vm)) return false;
        if (id == null || vm.id == null) return false;
        return Objects.equals(id, vm.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
