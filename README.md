# 플러스 주차 개인과제 진행 내용
이번 플러스주차 개인과제는 지금까지 배운 **JPA심화**, **테스트 코드**, **성능 최적화** 개념을 기반으로 진행되었다.
 
***

## 📚 필수기능
## 1. Transactional에 대한 이해
- `createReservation` 함수에서 **트랜잭션**을 적용하여, 하나라도 에러가 발생하면 모든 작업이 롤백되도록 구현했습니다.
-  작업이 정상적일 경우에만 저장되도록 `@Transactional` 어노테이션 추가 했습니다.
  - **결과** : **All or Nothing** 트랜잭션 보장

## 2. 인가에 대한 이해
- 기존 코드에서 `ADMIN` 권한 확인 API가 누락되어 있었던 문제를 개선했습니다.
- `/admin/*` URL에 대해 ADMIN 권한을 확인할 수 있도록 Interceptor를 추가했습니다.

## 3. N+1에 대한 이해
- 기존 코드에서 모든 예약을 조회 시, 연관된 엔티티(User, Item)을 조회하기 위해 **N+1 문제**가 발생했습니다.
- 이를 개선하기 위해 `JOIN FETCH`를 활용하여 `Reservation`, `User`, `Item` 데이터를 한 번의 쿼리로 모두 가져오도록 수정 했습니다.
  - 관련 메서드 : `findAllWithUserAndItem()`
  - 결과 : 동일한 데이터를 조회 시 N+1 문제를 해결하고 성능 최적화

## 4. DB 접근 최소화
- 기존 코드에서 `findById`로 사용자 ID 하나씩 조회하고 상태를 변경한 후 저장하던 방식을 개선했습니다.
- `userRepository.updateUserStatusInGroup()` 메서드를 통해 **userIds**와 **status**를 한 번의 쿼리로 처리하도록 변경 했습니다.

## 5. 동적 쿼리에 대한 이해
- 기존 코드에서 `userId`, `itemId` 조건에 따라 다른 메서드가 호출되는 방식과 연관 엔티티 조회 시 추가 쿼리가 실행되어 **N+1 문제**가 발생했습니다.
- **QueryDSL**을 사용해 동적 쿼리를 작성하고, **JOIN FETCH**를 통해 관련된 데이터를 한 번의 쿼리로 처리하도록 수정했습니다.

## 6. 필요한 부분만 갱신하기
- `Item` 엔티티의 `status` 컬럼은 `nullable = false`로 설정되어, 값이 없을 경우 **Column 'status' cannot be null** 에러가 발생했습니다.
- **`@DynamicInsert`** 어노테이션을 적용해, 값이 전달되지 않을 경우 데이터베이스 기본값(`PENDING`)이 자동으로 입력되도록 수정했습니다.

## 7. 리팩토링
- 기존 코드에서 발견된 여러 문제를 리팩토링했습니다:
    1. **if-else 제거**: 상태 검증 메서드(`validateStatusTransition`)와 상태 업데이트 메서드(`updateStatus`)를 활용하여 불필요한 `if-else` 구문을 제거했습니다.
    2. **중복된 `findById` 호출 제거**: `ReservationRepository`에 **`findByIdOrThrow`** 기본 메서드를 추가해 중복 로직을 해결했습니다.
    3. **상태 값 Enum 관리**: 문자열 기반 상태 관리에서 `ReservationStatus` Enum으로 전환해 상태를 명확히 관리하도록 개선했습니다.

## 8. 테스트 코드
- **PasswordEncoder 단위 테스트**
  - 암호화된 비밀번호는 **null이 아님**을 검즘 → `testEncode_NotNull()`
  - 암호화된 비밀번호는 **원본 비밀번호와 다름**을 검증 → `testEncode_NotSameAsRawPassword()`


- **Item Entity 테스트**
  - `Item` 엔티티의 `status` 컬럼이 `nullable = false`로 설정되어 있는지 검증했습니다.
  - JPQL을 이용해 `status`를 null로 업데이트하려 할 경우, **PersistenceException**이 발생하는지 확인
   - 관련 메서드 : `updateStatusToNull_shouldNull()`


***
## 📚 도전 기능
## 9. 테스트 코드
- **1. Controller Test 진행**
- `@WebMvcTest`를 활용해 **ReservationController**의 동작을 검증했습니다.
  - [POST /reservation] 요청이 올바른 데이터로 호출되었을 때, 성공 응답(`201 Created`)을 반환하는지 테스트했습니다.


- **2. 🥲Service Test** ----> 도전 실패

## 10. 테스트 환경 분리
- Local과 Test 환경의 DB를 분리하기 위해 환경별 yml 설정 파일을 작성했습니다.
  - `application-local.yml` → Local 환경 DB 연결 설정.
  - `application-test.yml` → Test 환경 DB 초기화 및 연결 설정.
- 각 환경에서 데이터베이스 설정이 정상적으로 작동하는지 확인했습니다.


## 11. 🥲AWS 활용 마스터 ----> 도전 실패
- AWS 활용을 통해 서비스를 배포하려 했지만, 이번 과제에서는 완수하지 못했습니다.
  - **교훈**: 배포 자동화와 AWS 설정에 대한 추가 학습 필요성을 확인했습니다.