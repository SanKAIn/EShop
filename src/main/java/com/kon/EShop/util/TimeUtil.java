package com.kon.EShop.util;

import javax.persistence.AttributeConverter;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TimeUtil {
    public static class LocalDateConverter implements AttributeConverter <LocalDate, Timestamp > {
        @Override
        public Timestamp convertToDatabaseColumn(LocalDate attribute) {
            return attribute != null ? Timestamp.valueOf(attribute.atStartOfDay()) : null;
        }

        @Override
        public LocalDate convertToEntityAttribute(Timestamp dbData) {
            return dbData != null ? dbData.toLocalDateTime().toLocalDate() : null;
        }
    }

    public static class LocalDateTimeConverter implements AttributeConverter <LocalDateTime, Timestamp > {
        @Override
        public Timestamp convertToDatabaseColumn(LocalDateTime attribute) {
            return attribute != null ? Timestamp.valueOf(attribute) : null;
        }

        @Override
        public LocalDateTime convertToEntityAttribute(Timestamp dbData) {
            return dbData != null ? dbData.toLocalDateTime() : null;
        }
    }
}
