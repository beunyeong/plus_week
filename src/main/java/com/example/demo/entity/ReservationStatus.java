package com.example.demo.entity;

public enum ReservationStatus {
    PENDING,        // 대기
    APPROVED,       // 승인
    CANCELLED,      // 취소
    EXPIRED;         // 만료

    // 상태 체크
    public void validateTransition(ReservationStatus newStatus) {
        switch (newStatus) {
            case APPROVED:
                if (this != PENDING) {
                    throw new IllegalArgumentException("PENDING 상태만 APPROVED로 변경 가능합니다.");
                }
                break;

            case CANCELLED:
                if (this == EXPIRED) {
                    throw new IllegalArgumentException("EXPIRED 상태인 예약은 취소할 수 없습니다.");
                }
                break;

            case EXPIRED:
                if (this != PENDING) {
                    throw new IllegalArgumentException("PENDING 상태만 EXPIRED로 변경 가능합니다.");
                }
                break;

            default:
                throw new IllegalArgumentException("올바르지 않은 상태: " + newStatus);
        }
    }
}

