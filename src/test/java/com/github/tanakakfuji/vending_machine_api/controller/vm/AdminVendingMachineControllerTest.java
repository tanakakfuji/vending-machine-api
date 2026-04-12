package com.github.tanakakfuji.vending_machine_api.controller.vm;

import com.github.tanakakfuji.vending_machine_api.domain.model.vm.Name;
import com.github.tanakakfuji.vending_machine_api.domain.model.vm.SlotCapacity;
import com.github.tanakakfuji.vending_machine_api.domain.model.vm.Status;
import com.github.tanakakfuji.vending_machine_api.domain.model.vm.VendingMachine;
import com.github.tanakakfuji.vending_machine_api.service.vm.AdminVendingMachineService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(AdminVendingMachineController.class)
public class AdminVendingMachineControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    AdminVendingMachineService adminVendingMachineService;

    @Nested
    class getVendingMachinesメソッドのテスト {
        @Test
        void 内部で例外が発生するときステータスコード500が返される() throws Exception {
            doThrow(new RuntimeException()).when(adminVendingMachineService).findAll();
            mockMvc.perform(
                            get("/api/admin/vending-machines")
                    )
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.message", is("サーバー内部でエラーが発生しました。管理者に連絡してください。")));
        }

        @Test
        void 例外が発生しないとき自販機の一覧が返される() throws Exception {
            List<VendingMachine> vendingMachines = List.of(
                    VendingMachine.reconstruct(1, new Name("サンプル1"), new SlotCapacity(10), Status.OPEN, new HashSet<>()),
                    VendingMachine.reconstruct(2, new Name("サンプル2"), new SlotCapacity(5), Status.CLOSED, new HashSet<>())
            );
            doReturn(vendingMachines).when(adminVendingMachineService).findAll();

            mockMvc.perform(
                            get("/api/admin/vending-machines")
                    ).andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].id", is(1)))
                    .andExpect(jsonPath("$[0].name.value", is("サンプル1")))
                    .andExpect(jsonPath("$[0].slotCapacity.value", is(10)))
                    .andExpect(jsonPath("$[0].status", is("OPEN")))
                    .andExpect(jsonPath("$[0].drinks", hasSize(0)))
                    .andExpect(jsonPath("$[1].id", is(2)))
                    .andExpect(jsonPath("$[1].name.value", is("サンプル2")))
                    .andExpect(jsonPath("$[1].slotCapacity.value", is(5)))
                    .andExpect(jsonPath("$[1].status", is("CLOSED")))
                    .andExpect(jsonPath("$[1].drinks", hasSize(0)));
        }

        @Test
        void 自販機が0件のとき空のリストが返される() throws Exception {
            List<VendingMachine> vendingMachines = List.of();
            doReturn(vendingMachines).when(adminVendingMachineService).findAll();
            mockMvc.perform(
                            get("/api/admin/vending-machines")
                    ).andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }
    }

    @Nested
    class registerVendingMachineメソッドのテスト {
        @Test
        void 入力値検証に失敗するときステータスコード400が返される() throws Exception {
            String requestBody = """
                    {
                        "name": "",
                        "slotCapacity": 0,
                        "status": "OPEN"
                    }
                    """;
            mockMvc.perform(
                            post("/api/admin/vending-machines")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(requestBody)
                    )
                    .andExpect(status().isBadRequest());
        }

        @Test
        void 自販機の名前が重複するときステータスコード400が返される() throws Exception {
            String requestBody = """
                    {
                        "name": "重複する名前",
                        "slotCapacity": 0,
                        "status": "OPEN"
                    }
                    """;
            doThrow(new IllegalArgumentException("入力された名前の自販機が既に存在します。重複しない名前を入力してください。"))
                    .when(adminVendingMachineService).create(any());
            mockMvc.perform(
                            post("/api/admin/vending-machines")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(requestBody)
                    )
                    .andExpect(status().isBadRequest());
        }

        @Test
        void 例外が発生しないときステータスコード201が返される() throws Exception {
            String requestBody = """
                    {
                        "name": "サンプル自販機",
                        "slotCapacity": 0,
                        "status": "OPEN"
                    }
                    """;
            doReturn(VendingMachine.reconstruct(1, new Name("サンプル自販機"), new SlotCapacity(0), Status.OPEN, new HashSet<>()))
                    .when(adminVendingMachineService).create(any());
            mockMvc.perform(
                            post("/api/admin/vending-machines")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(requestBody)
                    )
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location", "http://localhost/api/admin/vending-machines/1"));
        }
    }

    @Nested
    class updateVendingMachineメソッドのテスト {
        @Test
        void 入力値検証に失敗するときステータスコード400が返される() throws Exception {
            String requestBody = """
                    {
                        "name": "",
                        "slotCapacity": 0,
                        "status": "OPEN"
                    }
                    """;
            mockMvc.perform(
                            put("/api/admin/vending-machines/{id}", 1)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(requestBody)
                    )
                    .andExpect(status().isBadRequest());
        }

        @Test
        void 自販機の名前が重複するときステータスコード400が返される() throws Exception {
            String requestBody = """
                    {
                        "name": "重複する名前",
                        "slotCapacity": 0,
                        "status": "OPEN"
                    }
                    """;
            doThrow(new IllegalArgumentException("入力された名前の自販機が他に存在します。重複しない名前を入力してください。"))
                    .when(adminVendingMachineService).update(any(), any());
            mockMvc.perform(
                            put("/api/admin/vending-machines/{id}", 1)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(requestBody)
                    )
                    .andExpect(status().isBadRequest());
        }

        @Test
        void 例外が発生しないときステータスコード204が返される() throws Exception {
            String requestBody = """
                    {
                        "name": "更新後の名前",
                        "slotCapacity": 0,
                        "status": "OPEN"
                    }
                    """;
            doNothing().when(adminVendingMachineService).update(any(), any());
            mockMvc.perform(
                            put("/api/admin/vending-machines/{id}", 1)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(requestBody)
                    )
                    .andExpect(status().isNoContent());

            verify(adminVendingMachineService, times(1)).update(any(), any());
        }
    }

    @Nested
    class deleteVendingMachineメソッドのテスト {
        @Test
        void 指定された自販機が存在しないときステータスコード400が返される() throws Exception {
            doThrow(new IllegalArgumentException("指定された自販機が存在しません。"))
                    .when(adminVendingMachineService).deleteById(any());
            mockMvc.perform(
                            delete("/api/admin/vending-machines/{id}", 99)
                    )
                    .andExpect(status().isBadRequest());
        }

        @Test
        void 例外が発生しないときステータスコード204が返される() throws Exception {
            doNothing().when(adminVendingMachineService).deleteById(any());
            mockMvc.perform(
                            delete("/api/admin/vending-machines/{id}", 1)
                    )
                    .andExpect(status().isNoContent());
        }
    }
}
