package com.cookbook.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.List;

public class StepUpdateDTO {
    @NotNull(message = "ID процесса обязателен")
    private Long processId;

    @Positive(message = "Продолжительность должна быть положительной")
    private Integer duration;

    @Size(max = 500, message = "Заметка не должна превышать 500 символов")
    private String additionalNote;

    private List<StepIngredientCreateDTO> stepIngredients;

    // Геттеры и сеттеры
    public Long getProcessId() { return processId; }
    public void setProcessId(Long processId) { this.processId = processId; }
    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }
    public String getAdditionalNote() { return additionalNote; }
    public void setAdditionalNote(String additionalNote) { this.additionalNote = additionalNote; }
    public List<StepIngredientCreateDTO> getStepIngredients() { return stepIngredients; }
    public void setStepIngredients(List<StepIngredientCreateDTO> stepIngredients) { this.stepIngredients = stepIngredients; }
}