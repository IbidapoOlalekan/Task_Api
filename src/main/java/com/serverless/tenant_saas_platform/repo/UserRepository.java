package com.serverless.tenant_saas_platform.repo;

import com.serverless.tenant_saas_platform.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);
}
