package com.elearning.notification_service.dto;

public class ModuleCreatedDto {
    private Long courseId;
    private Long moduleId;
    private String title;
    // add other fields if course-service sends them

    // getters & setters
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    public Long getModuleId() { return moduleId; }
    public void setModuleId(Long moduleId) { this.moduleId = moduleId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
}
