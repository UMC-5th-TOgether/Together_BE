package com.backend.together.domain.post.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.lang.Nullable;

@SuppressWarnings({"rawtypes", "unchecked"})
public final class StringToEnumConverterFactory implements ConverterFactory<String, Enum> {


    @Override
    public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType) {
        return new StringToEnumConverter<>(targetType);
    }

    public static class StringToEnumConverter<T extends Enum<T>> implements Converter<String, T> {
        private final Class<T> enumType;

        public StringToEnumConverter(Class<T> enumType) {
            this.enumType = enumType;
        }

        @Override
        public T convert(String source) {
            try {
                return Enum.valueOf(enumType, source.toUpperCase());
            } catch (IllegalArgumentException e) {
                e.getMessage(); // "not exists enum value "
                return null;
            }
        }
    }
}