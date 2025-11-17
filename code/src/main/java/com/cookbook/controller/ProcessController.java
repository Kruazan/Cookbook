package com.cookbook.controller;

import com.cookbook.dto.ProcessCreateDTO;
import com.cookbook.dto.ProcessResponseDTO;
import com.cookbook.dto.ProcessUpdateDTO;
import com.cookbook.model.User;
import com.cookbook.repository.UserRepository;
import com.cookbook.service.ProcessService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для управления процессами.
 */
@RestController
@RequestMapping("/api/processes")
public class ProcessController {

    private final ProcessService processService;
    private final UserRepository userRepository;

    @Autowired
    public ProcessController(ProcessService processService, UserRepository userRepository) {
        this.processService = processService;
        this.userRepository = userRepository;
    }

    /**
     * Создание нового процесса.
     * @param dto DTO для создания процесса.
     * @return Данные созданного процесса.
     */
    @PostMapping
    public ResponseEntity<ProcessResponseDTO> createProcess(@Valid @RequestBody ProcessCreateDTO dto) {
        User user = getCurrentUser();
        ProcessResponseDTO process = processService.createProcess(dto, user);
        return ResponseEntity.ok(process);
    }

    /**
     * Получение процесса по ID.
     * @param id ID процесса.
     * @return Данные процесса.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProcessResponseDTO> getProcess(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        ProcessResponseDTO process = processService.getProcessById(id, userId);
        return ResponseEntity.ok(process);
    }

    /**
     * Получение всех процессов пользователя.
     * @return Список процессов.
     */
    @GetMapping
    public ResponseEntity<List<ProcessResponseDTO>> getAllProcesses() {
        Long userId = getCurrentUserId();
        List<ProcessResponseDTO> processes = processService.getAllProcessesByUser(userId);
        return ResponseEntity.ok(processes);
    }

    /**
     * Обновление процесса.
     * @param id ID процесса.
     * @param dto DTO с обновленными данными.
     * @return Обновленные данные процесса.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProcessResponseDTO> updateProcess(@PathVariable Long id, @Valid @RequestBody ProcessUpdateDTO dto) {
        Long userId = getCurrentUserId();
        ProcessResponseDTO updatedProcess = processService.updateProcess(id, userId, dto);
        return ResponseEntity.ok(updatedProcess);
    }

    /**
     * Удаление процесса.
     * @param id ID процесса.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProcess(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        processService.deleteProcess(id, userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Извлечение текущего пользователя из базы данных.
     * @return Объект User.
     */
    private User getCurrentUser() {
        Long userId = getCurrentUserId();
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с ID " + userId + " не найден"));
    }

    /**
     * Извлечение userId из JWT-токена.
     * @return ID текущего пользователя.
     */
    private Long getCurrentUserId() {
        Object credentials = SecurityContextHolder.getContext().getAuthentication().getCredentials();
        if (credentials instanceof Long) {
            return (Long) credentials;
        }
        throw new IllegalStateException("Не удалось извлечь userId из токена");
    }
}