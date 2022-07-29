package com.ordina.messageservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Getter
@Table(name = "messages")
@AllArgsConstructor
public class Message {

    protected Message() {}

    @Id
    @Column(nullable = false, updatable = false)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;

    @Column(nullable = false, updatable = false)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID userId;

    @Column(nullable = false)
    private String content;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Timestamp createdAt;
}
