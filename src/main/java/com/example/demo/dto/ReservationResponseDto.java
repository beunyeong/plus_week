package com.example.demo.dto;

import com.example.demo.entity.Reservation;
import com.example.demo.entity.ReservationStatus;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class ReservationResponseDto {
    private Long id;
    private ReservationStatus status;
    private String nickname;
    private String itemName;
    private LocalDateTime startAt;
    private LocalDateTime endAt;


    public ReservationResponseDto(Long id, String nickname, String itemName,
                                  LocalDateTime startAt, LocalDateTime endAt) {
        this.id = id;
        this.nickname = nickname;
        this.itemName = itemName;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public ReservationResponseDto(Reservation reservation) {
        this.id = reservation.getId();
        this.status = reservation.getStatus();
    }

}
