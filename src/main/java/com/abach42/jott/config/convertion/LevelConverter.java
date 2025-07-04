package com.abach42.jott.config.convertion;

import com.abach42.jott.user.UserRole;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class LevelConverter extends GenericEnumConverter<UserRole, Byte> {

    public LevelConverter() {
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