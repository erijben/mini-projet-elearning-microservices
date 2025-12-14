package com.elearning.courseservice.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateModuleRequest {

    @NotBlank
    private String title;

    private String content;
}
