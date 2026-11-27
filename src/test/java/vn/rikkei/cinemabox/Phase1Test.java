package vn.rikkei.cinemabox;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class Phase1Test {

    @Autowired
    private MockMvc mvc;

    @Test
    void getShowtimeById_thanhCong() throws Exception {
        mvc.perform(get("/api/showtimes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.movieTitle").value("Biệt Đội Sấm Sét"))
                .andExpect(jsonPath("$.roomName").value("Phòng IMAX"))
                .andExpect(jsonPath("$.startTime").value("2026-12-04T19:00:00"))
                .andExpect(jsonPath("$.price").value(120000))
                .andExpect(jsonPath("$.availableSeats").value(35));
    }

    @Test
    void getShowtimeById_khongTonTai_tra404() throws Exception {
        mvc.perform(get("/api/showtimes/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("NOT_FOUND"));
    }

    @Test
    void taoKhachHang_thanhCong() throws Exception {
        String json = """
                {
                    "fullName": "Trần Thị Thu",
                    "email": "thu.tran@example.com",
                    "phone": "0912345678",
                    "memberLevel": "STANDARD"
                }
                """;
        mvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.fullName").value("Trần Thị Thu"))
                .andExpect(jsonPath("$.email").value("thu.tran@example.com"))
                .andExpect(jsonPath("$.phone").value("0912345678"))
                .andExpect(jsonPath("$.memberLevel").value("STANDARD"))
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    void taoKhachHang_saiValidation_tra400() throws Exception {
        String json = """
                {
                    "fullName": "",
                    "email": "khong-phai-email",
                    "phone": "123",
                    "memberLevel": "VIP"
                }
                """;
        mvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("VALIDATION_FAILED"))
                .andExpect(jsonPath("$.details").isArray());
    }

    @Test
    void taoKhachHang_trungEmail_tra409() throws Exception {
        String json = """
                {
                    "fullName": "Nguyễn Văn Trùng",
                    "email": "an.nguyen@example.com"
                }
                """;
        mvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("CONFLICT"));
    }

    @Test
    void getKhachHangById_thanhCong() throws Exception {
        mvc.perform(get("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("an.nguyen@example.com"));
    }

    @Test
    void getKhachHangById_khongTonTai_tra404() throws Exception {
        mvc.perform(get("/api/customers/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("NOT_FOUND"));
    }

    @Test
    void getDanhSachKhachHang_thanhCong() throws Exception {
        mvc.perform(get("/api/customers?page=0&size=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(5))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").exists())
                .andExpect(jsonPath("$.totalPages").exists());
    }

    @Test
    void getDanhSachKhachHang_thamSoKhongHopLe_tra400() throws Exception {
        mvc.perform(get("/api/customers?page=-1&size=10"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("BAD_REQUEST"));

        mvc.perform(get("/api/customers?page=0&size=55"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("BAD_REQUEST"));

        mvc.perform(get("/api/customers?page=abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("BAD_REQUEST"));
    }

    @Test
    void postKhachHang_jsonKhongHopLe_tra400() throws Exception {
        mvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fullName\":}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("BAD_REQUEST"));
    }
}
