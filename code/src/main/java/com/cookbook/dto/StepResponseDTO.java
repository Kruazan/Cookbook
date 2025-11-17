package com.cookbook.dto;

import java.util.List;

public class StepResponseDTO {
    private Long id;
    private Integer stepOrder;
    private Long processId;
    private String processName;
    private Integer duration;
    private String additionalNote;
    private List<StepIngredientResponseDTO> stepIngredients;

    public StepResponseDTO(Long id, Integer stepOrder, Long processId, String processName, Integer duration, String additionalNote, List<StepIngredientResponseDTO> stepIngredients) {
        this.id = id;
        this.stepOrder = stepOrder;
        this.processId = processId;
        this.processName = processName;
        this.duration = duration;
        this.additionalNote = additionalNote;
        this.stepIngredients = stepIngredients;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getStepOrder() { return stepOrder; }
    public void setStepOrder(Integer stepOrder) { this.stepOrder = stepOrder; }
    public Long getProcessId() { return processId; }
    public void setProcessId(Long processId) { this.processId = processId; }
    public String getProcessName() { return processName; }
    public void setProcessName(String processName) { this.processName = processName; }
    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }
    public String getAdditionalNote() { return additionalNote; }
    public void setAdditionalNote(String additionalNote) { this.additionalNote = additionalNote; }
    public List<StepIngredientResponseDTO> getStepIngredients() { return stepIngredients; }
    public void setStepIngredients(List<StepIngredientResponseDTO> stepIngredients) { this.stepIngredients = stepIngredients; }
}