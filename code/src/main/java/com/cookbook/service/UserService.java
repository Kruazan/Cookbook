package com.cookbook.service;

import com.cookbook.dto.UserCreateDTO;
import com.cookbook.dto.UserResponseDTO;
import com.cookbook.dto.UserUpdateDTO;
import com.cookbook.mapper.CookbookMapper;
import com.cookbook.model.User;
import com.cookbook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CookbookMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, CookbookMapper mapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponseDTO createUser(UserCreateDTO dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Пользователь с таким именем уже существует");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует");
        }
        User user = mapper.toUser(dto);
        // Хэшируем пароль перед сохранением
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userRepository.save(user);
        return mapper.toUserResponseDTO(user);
    }

    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с ID " + id + " не найден"));
        return mapper.toUserResponseDTO(user);
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(mapper::toUserResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponseDTO updateUser(Long id, UserUpdateDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с ID " + id + " не найден"));
        if (dto.getUsername() != null && !dto.getUsername().equals(user.getUsername()) &&
                userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Пользователь с таким именем уже существует");
        }
        if (dto.getEmail() != null && !dto.getEmail().equals(user.getEmail()) &&
                userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует");
        }
        // Хэшируем новый пароль, если он предоставлен
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        mapper.updateUser(user, dto);
        user = userRepository.save(user);
        return mapper.toUserResponseDTO(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("Пользователь с ID " + id + " не найден");
        }
        userRepository.deleteById(id);
    }
}