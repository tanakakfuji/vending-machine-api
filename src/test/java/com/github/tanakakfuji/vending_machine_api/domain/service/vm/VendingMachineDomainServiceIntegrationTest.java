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
    class isDuplicateNameメソッドのテスト {
        @Test
        void nameがnullのとき例外が発生する() {
            Name name = null;
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> vendingMachineDomainService.isDuplicateName(name));
            assertEquals("自販機の名前を指定してください。", exception.getMessage());
        }

        @Test
        void nameがvending_machineテーブルに存在するときtrueが返される() {
            Name name = new Name("サンプル1");
            boolean actual = vendingMachineDomainService.isDuplicateName(name);
            assertTrue(actual);
        }

        @Test
        void nameがvending_machineテーブルに存在しないときfalseが返される() {
            Name name = new Name("存在しない名前");
            boolean actual = vendingMachineDomainService.isDuplicateName(name);
            assertFalse(actual);
        }
    }

    @Nested
    class isDuplicateNameExcludingIdメソッドのテスト {
        @Test
        void nameがnullのとき例外が発生する() {
            Name name = null;
            Integer id = 1;
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> vendingMachineDomainService.isDuplicateNameExcludingId(name, id));
            assertEquals("自販機の名前を指定してください。", exception.getMessage());
        }

        @Test
        void idがnullのとき例外が発生する() {
            Name name = new Name("サンプル1");
            Integer id = null;
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> vendingMachineDomainService.isDuplicateNameExcludingId(name, id));
            assertEquals("自販機を指定してください。", exception.getMessage());
        }

        @Test
        void nameがvending_machineテーブルに異なるidで存在するときtrueが返される() {
            Name name = new Name("サンプル1");
            Integer id = 2;
            boolean actual = vendingMachineDomainService.isDuplicateNameExcludingId(name, id);
            assertTrue(actual);
        }

        @Test
        void nameがvending_machineテーブルに同じidで存在するときfalseが返される() {
            Name name = new Name("サンプル1");
            Integer id = 1;
            boolean actual = vendingMachineDomainService.isDuplicateNameExcludingId(name, id);
            assertFalse(actual);
        }
    }
}
