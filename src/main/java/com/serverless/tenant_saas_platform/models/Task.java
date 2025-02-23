package com.serverless.tenant_saas_platform.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document(collection = "tasks")
public class Task {
    @Id
    private String id;

    private String title;
    private String description;
    private String status = "pending";
    private Integer priority;
    private LocalDate deadline;

    private String userId;

}
