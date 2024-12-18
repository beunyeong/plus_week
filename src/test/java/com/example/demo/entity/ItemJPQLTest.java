package com.example.demo.entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class ItemJPQLTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @DisplayName("JPQL UPDATE를 이용하여 status를 null로 설정시 예외 발생 확인")
    void updateStatusToNull_shouldNull() {
        Item item = new Item();
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setStatus("ACTIVE");

        entityManager.persist(item);
        entityManager.flush();      // 강제 반영

        // 강제 null 적용
        assertThatThrownBy(() -> {
            int updatedCount = entityManager.createQuery(
                            "UPDATE Item i SET i.status = null WHERE i.id = :id")
                    .setParameter("id", item.getId())
                    .executeUpdate();  //  쿼리 실행

        }).isInstanceOf(PersistenceException.class);

        }
    }

