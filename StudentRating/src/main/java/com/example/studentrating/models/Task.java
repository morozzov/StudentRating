package com.example.studentrating.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String title;

    private String about;

    private int cost;

    private int studentCount;

    @ManyToOne
    protected Teacher author;

    protected LocalDateTime deadLine;

    protected LocalDateTime createdAt;

    protected boolean active;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
