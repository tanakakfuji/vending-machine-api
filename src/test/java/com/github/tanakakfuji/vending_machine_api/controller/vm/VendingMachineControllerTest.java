package com.github.tanakakfuji.vending_machine_api.controller.vm;

import com.github.tanakakfuji.vending_machine_api.domain.model.vm.Name;
import com.github.tanakakfuji.vending_machine_api.domain.model.vm.SlotCapacity;
import com.github.tanakakfuji.vending_machine_api.domain.model.vm.Status;
import com.github.tanakakfuji.vending_machine_api.domain.model.vm.VendingMachine;
import com.github.tanakakfuji.vending_machine_api.service.vm.VendingMachineService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VendingMachineController.class)
public class VendingMachineControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    VendingMachineService vendingMachineService;

    @Nested
    class getVendingMachinesメソッドのテスト {
        @Test
        void 内部で例外が発生するときステータスコード500が返される() throws Exception {
            doThrow(new RuntimeException()).when(vendingMachineService).findOpenVms();

            mockMvc.perform(get("/api/vending-machines"))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.message", is("サーバー内部でエラーが発生しました。管理者に連絡してください。")));
        }

        @Test
        void 例外が発生しないとき公開中の自販機の一覧が返される() throws Exception {
            List<VendingMachine> vendingMachines = List.of(
                    VendingMachine.reconstruct(1, new Name("サンプル1"), new SlotCapacity(10), Status.OPEN, new HashSet<>()),
                    VendingMachine.reconstruct(2, new Name("サンプル2"), new SlotCapacity(5), Status.OPEN, new HashSet<>())
            );
            doReturn(vendingMachines).when(vendingMachineService).findOpenVms();

            mockMvc.perform(get("/api/vending-machines"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].id", is(1)))
                    .andExpect(jsonPath("$[0].name.value", is("サンプル1")))
                    .andExpect(jsonPath("$[0].slotCapacity.value", is(10)))
                    .andExpect(jsonPath("$[0].status", is("OPEN")))
                    .andExpect(jsonPath("$[0].drinks", hasSize(0)))
                    .andExpect(jsonPath("$[1].id", is(2)))
                    .andExpect(jsonPath("$[1].name.value", is("サンプル2")))
                    .andExpect(jsonPath("$[1].slotCapacity.value", is(5)))
                    .andExpect(jsonPath("$[1].status", is("OPEN")))
                    .andExpect(jsonPath("$[1].drinks", hasSize(0)));
        }

        @Test
        void 自販機が0件のとき空のリストが返される() throws Exception {
            List<VendingMachine> vendingMachines = List.of();
            doReturn(vendingMachines).when(vendingMachineService).findOpenVms();
            mockMvc.perform(
                            get("/api/vending-machines")
                    ).andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }
    }

    @Nested
    class purchaseDrinkメソッドのテスト {
        @Test
        void 入力値検証に失敗するときステータスコード400が返される() throws Exception {
            String requestBody = """
                    {
                        "money": -1
                    }
                    """;

            mockMvc.perform(
                            post("/api/vending-machines/1/purchase/1")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(requestBody)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.detail", is("Invalid request content.")));
        }

        @Test
        void 指定された飲み物が存在しないときステータスコード400が返される() throws Exception {
            String requestBody = """
                    {
                        "money": 200
                    }
                    """;
            doThrow(new IllegalArgumentException("指定された飲み物は存在しません。"))
                    .when(vendingMachineService).purchaseDrink(anyInt(), anyInt(), any());
            mockMvc.perform(
                            post("/api/vending-machines/1/purchase/99")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(requestBody)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message", is("指定された飲み物は存在しません。")));
        }

        @Test
        void 例外が発生しないときステータスコード200とお釣りが返される() throws Exception {
            String requestBody = """
                    {
                        "money": 200
                    }
                    """;
            doReturn(100).when(vendingMachineService).purchaseDrink(anyInt(), anyInt(), any());
            mockMvc.perform(
                            post("/api/vending-machines/1/purchase/1")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(requestBody)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is(100)));
        }
    }
}
