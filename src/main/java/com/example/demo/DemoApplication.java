package com.example.demo;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

    // JPAQueryFactory를 사용하기 위한 Bean 등록 --> 의존성 주입
    private final EntityManager entitymanager;

    public DemoApplication(EntityManager entitymanager) {
        this.entitymanager = entitymanager;
    }

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entitymanager);
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
