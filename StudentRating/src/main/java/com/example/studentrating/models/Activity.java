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
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String title;

    private int cost;

    @ManyToOne
    protected Teacher author;

    @ManyToOne
    protected Student student;

    protected LocalDateTime createdAt;

    private boolean isDisputed;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
