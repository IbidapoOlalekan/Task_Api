package com.serverless.tenant_saas_platform.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "landlords")
public class Landlord extends UserEntity{
    private Set<String> tenantIds = new HashSet<>();

    public Set<String> getTenantIds() {
        return tenantIds;
    }

    public void setTenantIds(Set<String> tenantIds) {
        this.tenantIds = tenantIds;
    }
}
