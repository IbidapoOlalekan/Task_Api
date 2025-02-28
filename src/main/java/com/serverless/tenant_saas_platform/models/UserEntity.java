package com.serverless.tenant_saas_platform.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Data
@Document(collection = "users")
@Getter
@AllArgsConstructor
@Setter
public class UserEntity {
    @Id
    private String  id;

    @NonNull
    private String email;
    @NonNull
    private String password;
    @NonNull
    private String username;
    @Getter
    private Set<RoleType> role;

    public UserEntity(){

    }

    public UserEntity(String email, String password) {
        this.email = email;
        this.password = password;
    }
    public UserEntity(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }
    public UserEntity(String email, String password, String username, Set<RoleType> role) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.role = role;
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



    public String getId() {
        return id;
    }

    public Set<RoleType> getRole() {
        return role;
    }

    public void setRole(Set<RoleType> role) {
        this.role = role;
    }

    public @NonNull String getUsername() {
        return username;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    public void setId(String id) {
        this.id = id;
    }


}
