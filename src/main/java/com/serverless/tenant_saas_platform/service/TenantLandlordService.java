package com.serverless.tenant_saas_platform.service;

import com.serverless.tenant_saas_platform.models.Landlord;
import com.serverless.tenant_saas_platform.models.Tenant;
import com.serverless.tenant_saas_platform.repo.LandlordRepository;
import com.serverless.tenant_saas_platform.repo.TenantRepository;
import com.serverless.tenant_saas_platform.repo.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
public class TenantLandlordService {
    private static final Logger logger = LoggerFactory.getLogger(TenantLandlordService.class);
    private final UserRepository userRepository;
    private final LandlordRepository landlordRepository;
    private final TenantRepository tenantRepository;

    public TenantLandlordService(UserRepository userRepository, LandlordRepository landlordRepository, TenantRepository tenantRepository) {
        this.userRepository = userRepository;
        this.landlordRepository = landlordRepository;
        this.tenantRepository = tenantRepository;
    }

    public String assignTenantToLandlord(String landlordId, String tenantId) {
        Landlord landlord = landlordRepository.findById(landlordId).orElseThrow(() -> new RuntimeException("Landlord not found"));
        Tenant tenant = (Tenant) userRepository.findById(tenantId).orElseThrow(() -> new RuntimeException("Tenant not found"));

        tenant.setLandlordId(landlordId);
        landlord.getTenantIds().add(tenantId);

        tenantRepository.save(tenant);
        landlordRepository.save(landlord);

        return "Tenant assigned to landlord successfully";
    }

    public Set<Landlord> getLandlordTenants(String landlordId) {
        return landlordRepository.findByIdIn(Set.of(landlordId));
    }

    public String getLandlordByTenantId(String tenantId) {
        logger.info("Fetching tenant with ID: {}", tenantId);
        Tenant tenant = tenantRepository.findById(tenantId).orElseThrow(() -> new RuntimeException("Tenant not found"));
        logger.info("Found tenant with ID: {}, landlordId: {}", tenant.getId(), tenant.getLandlordId());
        return tenant.getLandlordId() != null ? tenant.getLandlordId() : "No landlord assigned";
    }
}
