package com.cookbook.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "processes")
public class Process {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 1, max = 100)
    @Column(nullable = false)
    private String name; // Название

    @Size(max = 500)
    @Column
    private String note; // Заметка

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    @JsonBackReference
    private User createdBy; // Юзер, который создал

    // Конструкторы
    public Process() {}

    public Process(String name, String note, User createdBy) {
        this.name = name;
        this.note = note;
        this.createdBy = createdBy;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public User getCreatedBy() { return createdBy; }
    public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }
}