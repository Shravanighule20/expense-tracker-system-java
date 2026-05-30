package com.example.expense_tracker.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer month;

    private Integer year;

    private Double budgetAmount;

    @ManyToOne
    private User user;
}