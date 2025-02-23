package com.serverless.tenant_saas_platform.repo;

import com.serverless.tenant_saas_platform.models.Task;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TaskRepository extends MongoRepository<Task,String> {
    List<Task> findByUserId(String userId);
}
