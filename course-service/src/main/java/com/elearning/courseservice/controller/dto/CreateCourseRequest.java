package com.elearning.courseservice.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCourseRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    private String teacherId;
    private String level;

    // 👉 très important pour que request.getModules() existe
    private List<CreateModuleRequest> modules;
}
