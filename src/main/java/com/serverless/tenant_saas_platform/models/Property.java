package com.serverless.tenant_saas_platform.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "properties")
public class Property {
    @Id
    private String id;
    private String landlordId;
    private String address;
    private String description;
    private double rentAmount;
    private boolean available;
}
