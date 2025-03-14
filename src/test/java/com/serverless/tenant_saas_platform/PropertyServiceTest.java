package com.serverless.tenant_saas_platform;

import com.serverless.tenant_saas_platform.models.Property;
import com.serverless.tenant_saas_platform.repo.PropertyRepository;
import com.serverless.tenant_saas_platform.service.PropertyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PropertyServiceTest {

    @Mock
    private PropertyRepository propertyRepository;

    @InjectMocks
    private PropertyService propertyService;

    private Property property;

    @BeforeEach
    void setUp(){
        property = new Property("1", "landlord1", "123 Main Street", "Cozy Apartment", 1000.00, true);
    }

    @Test
    void testAddProperty(){
        when(propertyRepository.save(any(Property.class))).thenReturn(property);

        Property result = propertyService.addProperty("landlord1", property);

        assertNotNull(result);
        assertEquals("landlord1", result.getLandlordId());
        assertEquals("123 Main Street", result.getAddress());
        verify(propertyRepository, times(1)).save(property);
    }

    @Test
    void testGetAllProperties(){
        List<Property> properties = List.of(property);
        when(propertyRepository.findAll()).thenReturn(properties);

        List<Property> result = propertyService.getAllProperties();

        assertEquals(1, result.size());
        assertEquals(property, result.get(0));
        verify(propertyRepository, times(1)).findAll();
    }

    @Test
    void testUpdateProperty_Success(){
        Property updatedProperty = new Property("1", "landlord1", "456 Oak Street", "Updated", 1200.00, false);
        when(propertyRepository.findById("1")).thenReturn(Optional.of(property));
        when(propertyRepository.save(any(Property.class))).thenReturn(updatedProperty);

        Property result = propertyService.updateProperty("1", updatedProperty);

        assertEquals("456 Oak Street", result.getAddress());
        assertEquals(1200.0, result.getRentAmount());
        verify(propertyRepository, times(1)).findById("1");
        verify(propertyRepository, times(1)).save(any(Property.class));
    }

    @Test
    void testUpdateProperty_NotFound(){
        when(propertyRepository.findById("1")).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, ()->
                propertyService.updateProperty("1", property));

        assertEquals("Property is not found", exception.getMessage());
        verify(propertyRepository, times(1)).findById("1");
        verify(propertyRepository, never()).save(any());
    }

    @Test
    void testDeleteProperty_Success(){
        when(propertyRepository.existsById("1")).thenReturn(true);
        doNothing().when(propertyRepository).deleteById("1");

        propertyService.deleteProperty("1");

        verify(propertyRepository, times(1)).existsById("1");
        verify(propertyRepository, times(1)).deleteById("1");
    }

    @Test
    void testDeleteProperty_NotFound(){
        when(propertyRepository.existsById("1")).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () ->
                propertyService.deleteProperty("1"));

        assertEquals("Property is not found", exception.getMessage());
        verify(propertyRepository, times(1)).existsById("1");

        verify(propertyRepository, never()).deleteById("1");
    }

    @Test
    void testGetPropertyById_Success(){
        when(propertyRepository.findById("1")).thenReturn(Optional.of(property));

        Property result = propertyService.getPropertyById("1");

        assertEquals(property, result);
        verify(propertyRepository, times(1)).findById("1");

    }

    @Test
    void testGetPropertyById_NotFound(){
        when(propertyRepository.findById("1")).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () ->
                propertyService.getPropertyById("1"));

        assertEquals("Property is not found", exception.getMessage());
        verify(propertyRepository, times(1)).findById("1");
    }

    @Test
    void testGetPropertiesByLandlordId(){
        List<Property> properties = List.of(property);
        when(propertyRepository.findByLandlordId("landlord1")).thenReturn(properties);

        List<Property> result = propertyService.getPropertyByLandlordId("landlord1");

        assertEquals(1, result.size());
        assertEquals(property, result.get(0));
        verify(propertyRepository, times(1)).findByLandlordId("landlord1");
    }

}
