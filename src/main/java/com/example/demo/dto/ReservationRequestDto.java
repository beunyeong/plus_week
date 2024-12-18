package com.example.demo.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReservationRequestDto {
    private Long itemId;
    private Long userId;
    private LocalDateTime startAt;
    private LocalDateTime endAt;

    public ReservationRequestDto(Long itemId, Long userId, String startAt, String endAt) {
        this.itemId = itemId;
        this.userId = userId;
        this.startAt = LocalDateTime.parse(startAt);
        this.endAt = LocalDateTime.parse(endAt);
    }

}
