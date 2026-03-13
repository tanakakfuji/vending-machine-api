package com.github.tanakakfuji.vending_machine_api.domain.service.vm;

import com.github.tanakakfuji.vending_machine_api.domain.model.vm.Name;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
@Sql("VendingMachineDomainServiceIntegrationTest.sql")
public class VendingMachineDomainServiceIntegrationTest {
    @Autowired
    VendingMachineDomainService vendingMachineDomainService;

    @Nested
    class checkDuplicateNameメソッドのテスト {
        @Test
        void nameがnullのとき例外が発生する() {
            Name name = null;
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> vendingMachineDomainService.checkDuplicateName(name));
            assertEquals("自販機の名前を指定してください。", exception.getMessage());
        }

        @Test
        void nameがvending_machineテーブルに存在するとき例外が発生する() {
            Name name = new Name("サンプル1");
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> vendingMachineDomainService.checkDuplicateName(name));
            assertEquals("入力された名前の自販機が既に存在します。重複しない名前を入力してください。", exception.getMessage());
        }

        @Test
        void nameがvending_machineテーブルに存在しないとき例外が発生しない() {
            Name name = new Name("存在しない名前");
            assertDoesNotThrow(() -> vendingMachineDomainService.checkDuplicateName(name));
        }
    }

    @Nested
    class checkDuplicateNameExcludingIdメソッドのテスト {
        @Test
        void nameがnullのとき例外が発生する() {
            Name name = null;
            Integer id = 1;
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> vendingMachineDomainService.checkDuplicateNameExcludingId(name, id));
            assertEquals("自販機の名前を指定してください。", exception.getMessage());
        }

        @Test
        void idがnullのとき例外が発生する() {
            Name name = new Name("サンプル1");
            Integer id = null;
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> vendingMachineDomainService.checkDuplicateNameExcludingId(name, id));
            assertEquals("自販機を指定してください。", exception.getMessage());
        }

        @Test
        void nameがvending_machineテーブルに異なるidで存在するとき例外が発生する() {
            Name name = new Name("サンプル1");
            Integer id = 2;
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> vendingMachineDomainService.checkDuplicateNameExcludingId(name, id));
            assertEquals("入力された名前の自販機が他に存在します。重複しない名前を入力してください。", exception.getMessage());
        }

        @Test
        void nameがvending_machineテーブルに同じidで存在するとき例外が発生しない() {
            Name name = new Name("サンプル1");
            Integer id = 1;
            assertDoesNotThrow(() -> vendingMachineDomainService.checkDuplicateNameExcludingId(name, id));
        }
    }
}
