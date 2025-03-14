package com.serverless.tenant_saas_platform.config;

import com.serverless.tenant_saas_platform.auth.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.inMemoryAuthentication()
                .withUser("user")
                .password(passwordEncoder().encode("password"))
                .roles("USER");
        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        //Public endpoints
                        .requestMatchers("/api/auth/**").permitAll()//Allow login/register
                        .requestMatchers(HttpMethod.POST, "/api/test/reset").permitAll() //Allow reset endpoint
                        //Landlord-only endpoints
                        .requestMatchers(HttpMethod.POST, "/api/tenant-landlord/assign").hasRole("LANDLORD") // Assign tenants
                        .requestMatchers(HttpMethod.GET, "/api/tenant-landlord/landlord/*").hasRole("LANDLORD") //View Tenants
                        .requestMatchers(HttpMethod.GET,"/api/tenant-landlord/landlord/{landlordId}/tenants").hasRole("LANDLORD")
                        .requestMatchers(HttpMethod.POST, "/api/properties").hasRole("LANDLORD")
                        .requestMatchers(HttpMethod.PUT,"/api/properties/*").hasRole("LANDLORD")
                        .requestMatchers(HttpMethod.DELETE,"/api/properties/*").hasRole("LANDLORD")
                        .requestMatchers(HttpMethod.GET,"/api/properties/*").hasRole("LANDLORD")
                        //Tenant-Only Endpoints
                        .requestMatchers(HttpMethod.GET,"/api/tenant-landlord/tenants/*").hasRole("TENANT") //vIEW Landlord
                        .requestMatchers(HttpMethod.GET,"/api/properties/*").hasRole("TENANT")
                        //Tenant and Landlord endpoints
                        .requestMatchers(HttpMethod.GET, "/api/rent/history").hasAnyRole("TENANT", "LANDLORD")
                        .anyRequest().authenticated()                  // Secure endpoints
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); //Add JWR Filter

        return http.build();
    }
    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(
                User.withUsername("user")
                        .password(passwordEncoder().encode("password"))
                        .roles("USER")
                        .build()
        );
    }
}
