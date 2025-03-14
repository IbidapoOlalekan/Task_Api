package com.serverless.tenant_saas_platform.controller;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {
    private final MongoTemplate mongoTemplate;

    public TestController(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @PostMapping("/reset")
    public ResponseEntity<String> resetDatabase() {
        //Drop all collections
        mongoTemplate.getCollectionNames().forEach(collectionName ->
                mongoTemplate.dropCollection(collectionName));
        return ResponseEntity.ok("Database reset complete.");
    }
}
