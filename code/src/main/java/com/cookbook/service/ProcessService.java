package com.cookbook.service;

import com.cookbook.dto.ProcessCreateDTO;
import com.cookbook.dto.ProcessResponseDTO;
import com.cookbook.dto.ProcessUpdateDTO;
import com.cookbook.mapper.CookbookMapper;
import com.cookbook.model.Process;
import com.cookbook.model.User;
import com.cookbook.repository.ProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProcessService {

    private final ProcessRepository processRepository;
    private final CookbookMapper mapper;

    @Autowired
    public ProcessService(ProcessRepository processRepository, CookbookMapper mapper) {
        this.processRepository = processRepository;
        this.mapper = mapper;
    }

    @Transactional
    public ProcessResponseDTO createProcess(ProcessCreateDTO dto, User user) {
        if (processRepository.existsByNameAndCreatedById(dto.getName(), user.getId())) {
            throw new IllegalArgumentException("Процесс с таким названием уже существует у пользователя");
        }
        Process process = mapper.toProcess(dto, user);
        process = processRepository.save(process);
        return mapper.toProcessResponseDTO(process);
    }

    public ProcessResponseDTO getProcessById(Long id, Long userId) {
        Process process = processRepository.findByIdAndCreatedById(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("Процесс с ID " + id + " не найден для пользователя"));
        return mapper.toProcessResponseDTO(process);
    }

    public List<ProcessResponseDTO> getAllProcessesByUser(Long userId) {
        return processRepository.findByCreatedById(userId).stream()
                .map(mapper::toProcessResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProcessResponseDTO updateProcess(Long id, Long userId, ProcessUpdateDTO dto) {
        Process process = processRepository.findByIdAndCreatedById(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("Процесс с ID " + id + " не найден для пользователя"));
        if (dto.getName() != null && !dto.getName().equals(process.getName()) &&
                processRepository.existsByNameAndCreatedById(dto.getName(), userId)) {
            throw new IllegalArgumentException("Процесс с таким названием уже существует у пользователя");
        }
        mapper.updateProcess(process, dto);
        process = processRepository.save(process);
        return mapper.toProcessResponseDTO(process);
    }

    @Transactional
    public void deleteProcess(Long id, Long userId) {
        Process process = processRepository.findByIdAndCreatedById(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("Процесс с ID " + id + " не найден для пользователя"));
        processRepository.delete(process);
    }
}