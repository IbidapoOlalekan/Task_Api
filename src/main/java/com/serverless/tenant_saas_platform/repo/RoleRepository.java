package com.serverless.tenant_saas_platform.repo;

import com.serverless.tenant_saas_platform.models.Role;
import com.serverless.tenant_saas_platform.models.RoleType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(RoleType name);
}
