package com.serverless.tenant_saas_platform.repo;

import com.serverless.tenant_saas_platform.models.Property;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface PropertyRepository extends MongoRepository<Property, String> {
    List<Property> findByLandlordId(String landlordId);

}
