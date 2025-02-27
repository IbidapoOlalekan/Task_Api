package com.serverless.tenant_saas_platform.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "users")
@Getter
@AllArgsConstructor

@NoArgsConstructor
@Setter
public class UserEntity {
    @Id
    private String  id;

    @NonNull
    private String email;
    @NonNull
    private String password;
    @Getter
    private String role = "user";

    public UserEntity(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public @NonNull String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    public @NonNull String getPassword() {
        return password;
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
