package com.cookbook.dto;

public class ProcessResponseDTO {
    private Long id;
    private String name;
    private String note;
    private Long createdById;

    public ProcessResponseDTO(Long id, String name, String note, Long createdById) {
        this.id = id;
        this.name = name;
        this.note = note;
        this.createdById = createdById;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public Long getCreatedById() { return createdById; }
    public void setCreatedById(Long createdById) { this.createdById = createdById; }
}