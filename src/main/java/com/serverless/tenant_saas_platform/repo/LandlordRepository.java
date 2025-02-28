package com.serverless.tenant_saas_platform.repo;

import com.serverless.tenant_saas_platform.models.Landlord;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Set;

public interface LandlordRepository extends MongoRepository<Landlord, String> {
    Set<Landlord> findByIdIn(Set<String> tenantIds);
}
