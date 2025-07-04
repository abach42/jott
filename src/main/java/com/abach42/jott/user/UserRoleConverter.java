package com.abach42.jott.user;

import com.abach42.jott.config.convertion.GenericEnumConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class UserRoleConverter extends GenericEnumConverter<UserRole, Byte> {

    public UserRoleConverter() {
        super(UserRole.class);
    }
    
    @Override
    public UserRole convertToEntityAttribute(Byte dbData) {
        if (dbData == null) {
            return null;
        }
        
        // Use the static helper method for mapping legacy values
        return UserRole.fromValue(dbData);
    }
}