package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;


@Entity
@Getter
@Setter
@DynamicInsert      // 데이터를 보내지 않은 경우 기본값으로 입력(추가)
// TODO: 6. Dynamic Insert
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private User manager;

    @Column(nullable = false, columnDefinition = "varchar(20) default 'PENDING'")
    private String status;

    public Item(String name, String description, User manager, User owner) {
        this.name = name;
        this.description = description;
        this.manager = manager;
        this.owner = owner;
    }

    public Item() {}

}
