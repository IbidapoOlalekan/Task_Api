package com.serverless.tenant_saas_platform.controller;

import com.serverless.tenant_saas_platform.models.Property;
import com.serverless.tenant_saas_platform.models.UserEntity;
import com.serverless.tenant_saas_platform.service.PropertyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {
    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @PostMapping
    public ResponseEntity<Property> addProperty(
            @RequestBody Property property,
            Authentication authentication) {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        Property createdProperty = propertyService.addProperty(user.getId(), property);
        return ResponseEntity.ok(createdProperty);
    }

    @GetMapping
    public ResponseEntity<List<Property>> viewAllProperties(){
        List<Property> properties = propertyService.getAllProperties();
        return ResponseEntity.ok(properties);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Property> updateProperty(
            @PathVariable String id,
            @RequestBody Property property,
            Authentication authentication) {
        Property existingProperty = propertyService.getPropertyById(id);
        UserEntity user = (UserEntity) authentication.getPrincipal();
        if(!existingProperty.getLandlordId().equals(user.getId())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        Property updatedProperty = propertyService.updateProperty(id,property);
        return ResponseEntity.ok(updatedProperty);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProperty(
            @PathVariable String id,
            Authentication authentication
    ) {
        Property existingProperty = propertyService.getPropertyById(id);
        UserEntity user = (UserEntity) authentication.getPrincipal();
        if(!existingProperty.getLandlordId().equals(user.getId())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not authorized to delete this property");
        }

        propertyService.deleteProperty(id);
        return ResponseEntity.ok("Property Deleted Successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Property> getPropertyById(@PathVariable String id){
        Property property = propertyService.getPropertyById(id);
        return ResponseEntity.ok(property);
    }

    @GetMapping("/landlord/{landlordId}")
    public ResponseEntity<List<Property>> getPropertyByLandlordId(@PathVariable String landlordId){
        List<Property> properties = propertyService.getPropertyByLandlordId(landlordId);
        return ResponseEntity.ok(properties);
    }
}
