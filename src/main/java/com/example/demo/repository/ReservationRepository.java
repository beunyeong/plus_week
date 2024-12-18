package com.example.demo.repository;

import com.example.demo.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUserIdAndItemId(Long userId, Long itemId);

    List<Reservation> findByUserId(Long userId);

    List<Reservation> findByItemId(Long itemId);

    @Query("SELECT r FROM Reservation r " +
            "WHERE r.item.id = :id " +
            "AND NOT (r.endAt <= :startAt OR r.startAt >= :endAt) " +
            "AND r.status = 'APPROVED'")
    List<Reservation> findConflictingReservations(
            @Param("id") Long id,
            @Param("startAt") LocalDateTime startAt,
            @Param("endAt") LocalDateTime endAt
    );


    // 2. 전체 예약 조회
    @Query("SELECT r FROM  Reservation r JOIN FETCH r.user JOIN FETCH r.item")
    List<Reservation> findAllWithUserAndItem();


    // 7. 리팩로링 관련 메서드
    default Reservation findByIdOrThrow(Long id) {
        return findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID에 맞는 데이터가 존재 하지 않습니다."));
    }

}

