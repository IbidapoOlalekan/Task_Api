package com.serverless.tenant_saas_platform.repo;

import com.serverless.tenant_saas_platform.models.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<UserEntity, String> {
    UserEntity findByEmail(String email);
    boolean existsByEmail(String email);

}
