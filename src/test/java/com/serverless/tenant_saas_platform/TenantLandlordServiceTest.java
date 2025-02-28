package com.serverless.tenant_saas_platform;

import com.serverless.tenant_saas_platform.models.Landlord;
import com.serverless.tenant_saas_platform.models.Tenant;
import com.serverless.tenant_saas_platform.repo.LandlordRepository;
import com.serverless.tenant_saas_platform.repo.TenantRepository;
import com.serverless.tenant_saas_platform.repo.UserRepository;
import com.serverless.tenant_saas_platform.service.TenantLandlordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TenantLandlordServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private LandlordRepository  landlordRepository;

    @Mock
    private TenantRepository tenantRepository;

    @InjectMocks
    private TenantLandlordService tenantLandlordService;

    private Landlord landlord;
    private Tenant tenant;

    @BeforeEach
    void setUp(){
        landlord = new Landlord();
        landlord.setId("landlord1");
        landlord.setTenantIds(new HashSet<>());

        tenant = new Tenant();
        tenant.setId("tenant1");
        tenant.setLandlordId(null);
    }

    @Test
    void testAssignTenantToLandlord_Success(){
        when(landlordRepository.findById("landlord1")).thenReturn(Optional.of(landlord));
        when(userRepository.findById("tenant1")).thenReturn(Optional.of(tenant));
        when(tenantRepository.save(any(Tenant.class))).thenReturn(tenant);
        when(landlordRepository.save(any(Landlord.class))).thenReturn(landlord);

        String result = tenantLandlordService.assignTenantToLandlord("landlord1","tenant1");

        assertEquals("Tenant assigned to landlord successfully", result);
        assertEquals("landlord1", tenant.getLandlordId());
        assertTrue(landlord.getTenantIds().contains("tenant1"));
        verify(tenantRepository, times(1)).save(tenant);
        verify(landlordRepository, times(1)).save(landlord);
    }

    @Test
    void testAssignTenantToLandlord_LandlordNotFound(){
        when(landlordRepository.findById("landlord1")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                tenantLandlordService.assignTenantToLandlord("landlord1","tenant1"));
        assertEquals("Landlord not found", exception.getMessage());
    }

    @Test
    void testAssignTenantToLandlord_TenantNotFound(){
        when(landlordRepository.findById("landlord1")).thenReturn(Optional.of(landlord));
        when(userRepository.findById("tenant1")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, ()->
            tenantLandlordService.assignTenantToLandlord("landlord1","tenant1"));
        assertEquals("Tenant not found", exception.getMessage());
    }

    @Test
    void testGetLandlordTenants_Success(){
        Set<Landlord> landlords = Set.of(landlord);
        when(landlordRepository.findByIdIn(Set.of("landlord1"))).thenReturn(landlords);

        Set<Landlord> result = tenantLandlordService.getLandlordTenants("landlord1");

        assertEquals(landlords, result);
        verify(landlordRepository, times(1)).findByIdIn(Set.of("landlord1"));
    }

    @Test
    void testGetLandlordTenantsId_Success(){
        tenant.setLandlordId("landlord1");
        when(tenantRepository.findById("tenant1")).thenReturn(Optional.of(tenant));

        String result = tenantLandlordService.getLandlordByTenantId("tenant1");

        assertEquals("landlord1", result);
        verify(tenantRepository, times(1)).findById("tenant1");
    }

    @Test
    void testGetLandlordTenantsId_TenantNotFound(){
        when(tenantRepository.findById("tenant1")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                tenantLandlordService.getLandlordByTenantId("tenant1"));
        assertEquals("Tenant not found", exception.getMessage());
    }
}
