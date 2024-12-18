package com.example.demo.service;

import com.example.demo.dto.ReservationResponseDto;
import com.example.demo.entity.*;
import com.example.demo.exception.ReservationConflictException;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.UserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final JPAQueryFactory jpaQueryFactory;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final RentalLogService rentalLogService;

    public ReservationService(ReservationRepository reservationRepository,
                              JPAQueryFactory jpaQueryFactory,
                              ItemRepository itemRepository,
                              UserRepository userRepository,
                              RentalLogService rentalLogService) {
        this.reservationRepository = reservationRepository;
        this.jpaQueryFactory = jpaQueryFactory;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.rentalLogService = rentalLogService;
    }

    // TODO: 1. 트랜잭션 이해
    // 1. 예약 생성
    @Transactional      // 어노테이션 추가
    public void createReservation(Long itemId, Long userId, LocalDateTime startAt, LocalDateTime endAt) {
        // 쉽게 데이터를 생성하려면 아래 유효성검사 주석 처리
        List<Reservation> haveReservations = reservationRepository.findConflictingReservations(itemId, startAt, endAt);
        if(!haveReservations.isEmpty()) {
            throw new ReservationConflictException("해당 물건은 이미 그 시간에 예약이 있습니다.");
        }

        // 아이템과 사용자 조회
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID에 맞는 값이 존재하지 않습니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID에 맞는 값이 존재하지 않습니다."));

        // 예약 생성 및 저장
        Reservation reservation = new Reservation(item, user, ReservationStatus.PENDING, startAt, endAt);
        Reservation savedReservation = reservationRepository.save(reservation);

        // 로그 생성 및 저장
        RentalLog rentalLog = new RentalLog(savedReservation, "로그 메세지", "CREATE");
        rentalLogService.save(rentalLog);
    }

    // TODO: 3. N+1 문제
    // 2. 전체 예약 조회
    public List<ReservationResponseDto> getReservations() {
        List<Reservation> reservations = reservationRepository.findAllWithUserAndItem();

        return reservations.stream().map(reservation -> {
            User user = reservation.getUser();
            Item item = reservation.getItem();

            return new ReservationResponseDto(
                    reservation.getId(),
                    user.getNickname(),
                    item.getName(),
                    reservation.getStartAt(),
                    reservation.getEndAt()
            );
        }).toList();
    }

    // TODO: 5. QueryDSL 검색 개선
    // QueryDRL을 사용하여, 검색 조건에 따라 예약 데이터를 조회하고 DTO로 변환
    public List<ReservationResponseDto> searchAndConvertReservations(Long userId, Long itemId) {
        // searchReservations 메서드를 호출하여, 조건에 맞는 Reservation 데이터를 조회
        List<Reservation> reservations = searchReservations(userId, itemId);
        // 조회된 Reservation 리스트를 ReservationResponseDto 리스트로 변환하여 반환
        return convertToDto(reservations);
    }

    // QueryDRL을 사용하여, 동적 검색을 수행하는 메서드
    public List<Reservation> searchReservations(Long userId, Long itemId) {

        // QueryDRL에서 생성된 Q클래스 인스턴스를 선언하여 각 엔티티를 참조
        QReservation reservation = QReservation.reservation;
        QUser user = QUser.user;
        QItem item = QItem.item;

        // QueryDRL을 통해 동적 쿼리를 작성하고 실행
        return jpaQueryFactory
                .selectFrom(reservation)    // 예약데이터를 기준으로 쿼리 진행
                .leftJoin(reservation.user, user).fetchJoin() //user 데이터를 함께 로드 (fetchJoin으로 N+1 문제 방지)
                .leftJoin(reservation.item, item).fetchJoin() //item 데이터를 함께 로드 (fetchJoin으로 N+1 문제 방지)
                .where(
                        userId !=null ? reservation.user.id.eq(userId) : null,  // userId 조건 추가(존재할 경우)
                        itemId !=null ? reservation.item.id.eq(itemId) : null   // itemId 조건 추가(존재할 경우)
                )
                .fetch();

    }

    private List<ReservationResponseDto> convertToDto(List<Reservation> reservations) {
        return reservations.stream()
                .map(reservation -> new ReservationResponseDto(
                        reservation.getId(),
                        reservation.getUser().getNickname(),
                        reservation.getItem().getName(),
                        reservation.getStartAt(),
                        reservation.getEndAt()
                ))
                .toList();
    }

    // TODO: 7. 리팩토링
    @Transactional
    public Reservation updateReservationStatus(Long reservationId, ReservationStatus status) {

        // 예약 ID로 데이터베이스에서 예약정보를 가져오고, 없으면 예외 처리
        Reservation reservation = reservationRepository.findByIdOrThrow(reservationId);

        // 상태 전환 검증
        reservation.validateStatusTransition(status);

        // 상태 업데이트
        reservation.updateStatus(status);

        return reservation;

    }

}
