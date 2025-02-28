package com.serverless.tenant_saas_platform.repo;

import com.serverless.tenant_saas_platform.models.Tenant;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TenantRepository extends MongoRepository<Tenant, String> {
    Optional<Tenant> findByLandlordId(String landlordId);
}
