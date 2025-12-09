package com.elearning.courseservice.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateModuleRequest {

    @NotBlank
    private String title;

    private String content;

    private Integer orderIndex;
}
