package com.company.entity;
// PROJECT NAME Kun_Uz
// TIME 16:59
// MONTH 06
// DAY 20

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "attach")
public class AttachEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String uuid;
    @Column(name = "origin_name",nullable = false)
    private String originName;
    @Column(nullable = false)
    private String type;
    @Column(nullable = false)
    private String path;
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();
}
