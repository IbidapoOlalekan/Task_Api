package com.serverless.tenant_saas_platform.controller;

import com.serverless.tenant_saas_platform.models.Landlord;
import com.serverless.tenant_saas_platform.service.TenantLandlordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/tenant-landlord")
public class TenantLandlordController {
    private final TenantLandlordService tenantLandlordService;

    public TenantLandlordController(TenantLandlordService tenantLandlordService) {
        this.tenantLandlordService = tenantLandlordService;
    }

    @PostMapping("/assign")
    public ResponseEntity<String> assignTenanttoLandlord(
            @RequestParam String landlordId,
            @RequestParam String tenantId) {
        try {
            String result = tenantLandlordService.assignTenantToLandlord(landlordId, tenantId);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

    @GetMapping("/landlord/{landlordId}/tenants")
    public ResponseEntity<Set<Landlord>> getLandlordTenants(
            @PathVariable String landlordId){
        Set<Landlord> landlords = tenantLandlordService.getLandlordTenants(landlordId);
        return ResponseEntity.ok(landlords);
    }

    @GetMapping("/tenants/{tenantId}/landlord")
    public ResponseEntity<String> getLandlordByTenantId(
            @PathVariable String tenantId){
        try {
            String landlordId = tenantLandlordService.getLandlordByTenantId(tenantId);
            return ResponseEntity.ok(landlordId);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }
}
