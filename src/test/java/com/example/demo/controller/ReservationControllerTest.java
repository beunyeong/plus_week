package com.example.demo.controller;

import com.example.demo.dto.ReservationRequestDto;
import com.example.demo.service.ReservationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ReservationController.class)
@ActiveProfiles("test")
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservationService reservationService;

    @Test
    @DisplayName("신규 예약 생성 테스트 확인")
    void createReservation_shouldSuccess() throws Exception {


        // Given: 신규 예약시 필요한 데이터 준비
        ReservationRequestDto reservationRequestDto = new ReservationRequestDto(
                1L, 2L,
                "2024-12-18T09:00:00",
                "2024-12-19T18:00:00");

        // When: 신규 예약 post 호출
        mockMvc.perform(post("/reservation")
                .contentType(MediaType.APPLICATION_JSON)        // JSON 형식 알림
                .content("""
                {
                "itemId": 1,
                "userId": 2,
                "startAt": "2024-12-18T09:00:00",
                "endAt": "2024-12-19T18:00:00"
                }
                """))
                // Then: 결과 확인
                .andExpect(status().isCreated());

    }

}