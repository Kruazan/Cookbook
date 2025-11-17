package com.cookbook.dto;

import jakarta.validation.constraints.Size;

public class ProcessUpdateDTO {
    @Size(min = 1, max = 100, message = "Название должно быть от 1 до 100 символов")
    private String name;

    @Size(max = 500, message = "Заметка не должна превышать 500 символов")
    private String note;

    // Геттеры и сеттеры
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}