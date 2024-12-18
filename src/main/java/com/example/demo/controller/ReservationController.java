package com.example.demo.controller;

import com.example.demo.dto.ReservationRequestDto;
import com.example.demo.dto.ReservationResponseDto;
import com.example.demo.entity.Reservation;
import com.example.demo.entity.ReservationStatus;
import com.example.demo.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;
    private final StandardServletMultipartResolver standardServletMultipartResolver;

    public ReservationController(ReservationService reservationService,
                                 StandardServletMultipartResolver standardServletMultipartResolver) {
        this.reservationService = reservationService;
        this.standardServletMultipartResolver = standardServletMultipartResolver;
    }

    // 1. 예약 생성
    @PostMapping
    public void createReservation(@RequestBody ReservationRequestDto reservationRequestDto) {
        reservationService.createReservation(reservationRequestDto.getItemId(),
                reservationRequestDto.getUserId(),
                reservationRequestDto.getStartAt(),
                reservationRequestDto.getEndAt());
    }

    @PatchMapping("/{id}/update-status")
    public void updateReservation(@PathVariable Long id, @RequestBody String status) {
        ReservationStatus reservationStatus = ReservationStatus.valueOf(status.toUpperCase());
        reservationService.updateReservationStatus(id, reservationStatus);
    }

    // 2. 전체 예약 조회
    @GetMapping
    public void findAll() {
        reservationService.getReservations();
    }

    @GetMapping("/search")
    public void searchAll(@RequestParam(required = false) Long userId,
                          @RequestParam(required = false) Long itemId) {
        reservationService.searchAndConvertReservations(userId, itemId);
    }

    @PutMapping("/{reservationId}/status")
    public ResponseEntity<ReservationResponseDto> updateStatus(
            @PathVariable Long reservationId,
            @RequestBody ReservationResponseDto reservationResponseDto) {

        // 상태 업데이트
        ReservationStatus newStatus = reservationResponseDto.getStatus();
        Reservation updatedReservation = reservationService.updateReservationStatus(reservationId, newStatus);

        return ResponseEntity.ok(new ReservationResponseDto(updatedReservation));

    }

}