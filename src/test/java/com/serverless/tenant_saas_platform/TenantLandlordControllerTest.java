package com.serverless.tenant_saas_platform;

import com.serverless.tenant_saas_platform.controller.TenantLandlordController;
import com.serverless.tenant_saas_platform.models.Landlord;
import com.serverless.tenant_saas_platform.service.TenantLandlordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class TenantLandlordControllerTest {

    @Mock
    private TenantLandlordService tenantLandlordService;

    @InjectMocks
    private TenantLandlordController tenantLandlordController;

    private MockMvc mockMvc;
    private Landlord landlord;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(tenantLandlordController).build();

        landlord = new Landlord();
        landlord.setId("landlord1");
        landlord.setTenantIds(Set.of("tenant1"));
    }

    @Test
    void testAssignTenantToLandlord_Success() throws Exception {
        when(tenantLandlordService.assignTenantToLandlord("landlord1", "tenant1"))
                .thenReturn("Tenant assigned to landlord successfully");

        mockMvc.perform(post("/api/tenant-landlord/assign")
                .param("landlordId", "landlord1")
                .param("tenantId", "tenant1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Tenant assigned to landlord successfully"));

        verify(tenantLandlordService, times(1)).assignTenantToLandlord("landlord1", "tenant1");
    }

    @Test
    void testGetLandlordTenants_Success() throws Exception {
        when(tenantLandlordService.getLandlordTenants("landlord1")).thenReturn(Set.of(landlord));
        mockMvc.perform(get("/api/tenant-landlord/landlord/landlord1/tenants")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("landlord1"))
                .andExpect(jsonPath("$[0].tenantIds[0]").value("tenant1"));

        verify(tenantLandlordService, times(1)).getLandlordTenants("landlord1");
    }

    @Test
    void testGetLandlordByTenantId_Success() throws Exception {
        when(tenantLandlordService.getLandlordByTenantId("tenant1")).thenReturn("landlord1");

        mockMvc.perform(get("/api/tenant-landlord/tenants/tenant1/landlord")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("landlord1"));

        verify(tenantLandlordService, times(1)).getLandlordByTenantId("tenant1");
    }

    @Test
    void testAssignTenantToLandlord_Failure() throws Exception {
        when(tenantLandlordService.assignTenantToLandlord("landlord1", "tenant1"))
                .thenThrow(new RuntimeException("Landlord not found"));

        mockMvc.perform(post("/api/tenant-landlord/assign")
                .param("landlordId", "landlord1")
                .param("tenantId", "tenant1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Landlord not found"));

        verify(tenantLandlordService, times(1)).assignTenantToLandlord("landlord1", "tenant1");
    }
}
