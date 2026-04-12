package com.github.tanakakfuji.vending_machine_api.controller.drink;

import com.github.tanakakfuji.vending_machine_api.service.drink.AdminDrinkService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AdminDrinkController.class)
public class AdminDrinkControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    AdminDrinkService adminDrinkService;

    @Nested
    class registerDrinkメソッドのテスト {
        @Test
        void DrinkListInputの入力値検証に失敗するときステータスコード400が返される() throws Exception {
            String requestBody = """
                    {
                        "drinks": []
                    }
                    """;
            mockMvc.perform(post("/api/admin/vending-machines/{vmId}/drinks", 1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void DrinkInputの入力値検証に失敗するときステータスコード400が返される() throws Exception {
            String requestBody = """
                    {
                        "drinks": [
                            {
                                "name": "",
                                "volume": 500,
                                "price": 100,
                                "stock": 10
                            }
                        ]
                    }
                    """;
            mockMvc.perform(post("/api/admin/vending-machines/{vmId}/drinks", 1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void 指定された自販機が存在しないときステータスコード400が返される() throws Exception {
            String requestBody = """
                    {
                        "drinks": [
                            {
                                "name": "更新後の名前",
                                "volume": 500,
                                "price": 100,
                                "stock": 10
                            }
                        ]
                    }
                    """;
            doThrow(new IllegalArgumentException("指定された自販機は存在しません"))
                    .when(adminDrinkService).create(anyInt(), any());
            mockMvc.perform(post("/api/admin/vending-machines/{vmId}/drinks", 99)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void 例外が発生しないときステータスコード201が返される() throws Exception {
            String requestBody = """
                    {
                        "drinks": [
                            {
                                "name": "更新後の名前",
                                "volume": 500,
                                "price": 100,
                                "stock": 10
                            }
                        ]
                    }
                    """;
            mockMvc.perform(post("/api/admin/vending-machines/{vmId}/drinks", 1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isCreated());
            verify(adminDrinkService, times(1)).create(anyInt(), any());
        }
    }

    @Nested
    class updateDrinkメソッドのテスト {
        @Test
        void 入力値検証に失敗するときステータスコード400が返される() throws Exception {
            String requestBody = """
                    {
                        "name": "",
                        "volume": 500,
                        "price": 100,
                        "stock": 10
                    }
                    """;
            mockMvc.perform(put("/api/admin/vending-machines/{vmId}/drinks/{drinkId}", 1, 1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void 指定された飲み物が存在しないときステータスコード400が返される() throws Exception {
            String requestBody = """
                    {
                        "name": "更新後の名前",
                        "volume": 500,
                        "price": 100,
                        "stock": 10
                    }
                    """;
            doThrow(new IllegalArgumentException("指定された飲み物は存在しません"))
                    .when(adminDrinkService).update(anyInt(), anyInt(), any());
            mockMvc.perform(put("/api/admin/vending-machines/{vmId}/drinks/{drinkId}", 1, 99)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void 例外が発生しないときステータスコード204が返される() throws Exception {
            String requestBody = """
                    {
                        "name": "更新後の名前",
                        "volume": 500,
                        "price": 100,
                        "stock": 10
                    }
                    """;
            mockMvc.perform(put("/api/admin/vending-machines/{vmId}/drinks/{drinkId}", 1, 1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isNoContent());
            verify(adminDrinkService, times(1)).update(anyInt(), anyInt(), any());
        }
    }

    @Nested
    class deleteDrinkメソッドのテスト {
        @Test
        void 指定された飲み物が存在しないときステータスコード400が返される() throws Exception {
            doThrow(new IllegalArgumentException("指定された飲み物は存在しません"))
                    .when(adminDrinkService).deleteById(anyInt(), anyInt());
            mockMvc.perform(delete("/api/admin/vending-machines/{vmId}/drinks/{drinkId}", 1, 99))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void 例外が発生しないときステータスコード204が返される() throws Exception {
            mockMvc.perform(delete("/api/admin/vending-machines/{vmId}/drinks/{drinkId}", 1, 1))
                    .andExpect(status().isNoContent());
            verify(adminDrinkService, times(1)).deleteById(anyInt(), anyInt());
        }
    }
}
