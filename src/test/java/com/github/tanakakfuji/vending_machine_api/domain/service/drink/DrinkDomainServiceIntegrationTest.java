package com.github.tanakakfuji.vending_machine_api.domain.service.drink;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
@Sql("DrinkDomainServiceIntegrationTest.sql")
public class DrinkDomainServiceIntegrationTest {
    @Autowired
    DrinkDomainService drinkDomainService;

    @Nested
    class existsByVmIdメソッドのテスト {
        @Test
        void vmIdがnullのとき例外が発生する() {
            Integer vmId = null;
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> drinkDomainService.existsByVmId(vmId));
            assertEquals("対象の自販機が正しく指定されていません。", exception.getMessage());
        }

        @Test
        void vmIdがvending_machineテーブルに存在するときtrueが返される() {
            Integer vmId = 1;
            boolean actual = drinkDomainService.existsByVmId(vmId);
            assertTrue(actual);
        }

        @Test
        void vmIdがvending_machineテーブルに存在しないときfalseが返される() {
            Integer vmId = 10;
            boolean actual = drinkDomainService.existsByVmId(vmId);
            assertFalse(actual);
        }
    }
}
