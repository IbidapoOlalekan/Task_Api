package com.serverless.tenant_saas_platform.service;

import com.serverless.tenant_saas_platform.models.Property;
import com.serverless.tenant_saas_platform.repo.PropertyRepository;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PropertyService {
    private final PropertyRepository propertyRepository;

    public PropertyService(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    public Property addProperty(String landlordId, Property property) {
        property.setLandlordId(landlordId);
        return propertyRepository.save(property);
    }

    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }

    public Property updateProperty(String propertyId, Property updatedProperty) {
        Optional<Property> existingProperty = propertyRepository.findById(propertyId);
        if(existingProperty.isEmpty()){
            throw new RuntimeException("Property is not found");
        }

        Property property = existingProperty.get();
        property.setAddress(updatedProperty.getAddress());
        property.setDescription(updatedProperty.getDescription());
        property.setRentAmount(updatedProperty.getRentAmount());
        property.setAvailable(updatedProperty.isAvailable());
        return propertyRepository.save(property);
    }

    public void deleteProperty(String propertyId) {
        if(!propertyRepository.existsById(propertyId)) {
            throw new RuntimeException("Property is not found");
        }
        propertyRepository.deleteById(propertyId);
    }

    public Property getPropertyById(String propertyId) {
        return propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property is not found"));
    }

    public List<Property> getPropertyByLandlordId(String landlordId) {
        return propertyRepository.findByLandlordId(landlordId);
    }
}
