package com.cookbook.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public class UserUpdateDTO {
    @Size(min = 3, max = 50, message = "Имя пользователя должно быть от 3 до 50 символов")
    private String username;

    @Size(min = 6, message = "Пароль должен быть не менее 6 символов")
    private String password;

    @Email(message = "Некорректный email")
    private String email;

    // Геттеры и сеттеры
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}