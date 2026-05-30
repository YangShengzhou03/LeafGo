package com.leafgo.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 255)
    private String logo;

    @Column(length = 50)
    private String scale;

    @Column(length = 100)
    private String industry;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 50)
    private String province;

    @Column(length = 50)
    private String city;

    @Column(length = 50)
    private String district;

    @Column(length = 255)
    private String address;

    @Column(length = 255)
    private String website;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean verified = false;

    @Column(columnDefinition = "TEXT")
    private String benefits;

    @Column(name = "work_time", length = 100)
    private String workTime;

    @Column(name = "boss_info", columnDefinition = "TEXT")
    private String bossInfo;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
