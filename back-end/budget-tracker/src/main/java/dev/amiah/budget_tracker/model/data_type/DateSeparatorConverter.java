package dev.amiah.budget_tracker.model.data_type;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class DateSeparatorConverter implements AttributeConverter<DateSeparator, String> {

    @Override
    public String convertToDatabaseColumn(DateSeparator dateSeparator) {
        return dateSeparator.getSeparator();
    }

    @Override
    public DateSeparator convertToEntityAttribute(String separator) {
        return DateSeparator.fromString(separator);
    }

}
