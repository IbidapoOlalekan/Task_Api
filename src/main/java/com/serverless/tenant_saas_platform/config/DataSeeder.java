package com.serverless.tenant_saas_platform.config;

import com.serverless.tenant_saas_platform.models.Role;
import com.serverless.tenant_saas_platform.models.RoleType;
import com.serverless.tenant_saas_platform.repo.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public DataSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    @Override
    public void run(String... args) throws Exception {
        Arrays.stream(RoleType.values()).forEach(roleType -> {
            if(roleRepository.findByName(roleType).isEmpty()) {
                Role role = new Role();
                role.setName(roleType);
                roleRepository.save(role);
                System.out.println("Inserted role: " + roleType);
            }
        });
    }
}
