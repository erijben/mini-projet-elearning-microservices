package com.elearning.notification_service.listener;

import com.elearning.notification_service.config.RabbitConfig;
import com.elearning.notification_service.model.Notification;
import com.elearning.notification_service.repository.NotificationRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ModuleCreatedListener {

    private final NotificationRepository repo;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ModuleCreatedListener(NotificationRepository repo) {
        this.repo = repo;
    }

    @RabbitListener(queues = RabbitConfig.NEW_MODULE_QUEUE)
    public void receive(String payload) {
        try {
            JsonNode node = objectMapper.readTree(payload);
            Long courseId = node.has("courseId") ? node.get("courseId").asLong() : null;
            Long moduleId = node.has("moduleId") ? node.get("moduleId").asLong() : null;
            String title = node.has("title") ? node.get("title").asText() : "Nouveau module";

            // Simple behavior: create a Notification record (studentId unknown here)
            Notification n = new Notification(null, courseId, moduleId,
                    "Nouveau module ajouté : " + title);
            repo.save(n);

            System.out.println("[notification-service] Received new-module: " + payload);
        } catch (Exception e) {
            System.err.println("Failed to parse message: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
