package com.abach42.jott.config.convertion;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public abstract class GenericEnumConverter<E extends Enum<E> & ConvertibleEnum<T>, T>
        implements AttributeConverter<E, T> {

    private final Class<E> enumType;

    protected GenericEnumConverter(Class<E> enumType) {
        this.enumType = enumType;
    }

    @Override
    public T convertToDatabaseColumn(E attribute) {
        return attribute != null ? attribute.getValue() : null;
    }

    @Override
    public E convertToEntityAttribute(T dbData) {
        if (dbData == null) {
            return null;
        }
        for (E enumConstant : enumType.getEnumConstants()) {
            if (enumConstant.getValue().equals(dbData)) {
                return enumConstant;
            }
        }
        throw new IllegalArgumentException(
                "Unknown value '" + dbData + "' for enum type " + enumType.getName());
    }
}