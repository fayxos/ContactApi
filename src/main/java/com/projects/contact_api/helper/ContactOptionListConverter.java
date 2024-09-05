package com.projects.contact_api.helper;

import com.projects.contact_api.model.ContactOptions;
import jakarta.persistence.AttributeConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ContactOptionListConverter implements AttributeConverter<List<ContactOptions>, String> {

    private static final String SEPARATOR = ";";

    @Override
    public String convertToDatabaseColumn(List<ContactOptions> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }

        return list.stream()
                .map(co -> String.valueOf(co.getNumber()))
                .collect(Collectors.joining(SEPARATOR));
    }

    @Override
    public List<ContactOptions> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return new ArrayList<>();
        }

        return Arrays.stream(dbData.split(SEPARATOR))
                .map(i -> ContactOptions.fromNumber(Integer.parseInt(i)))
                .collect(Collectors.toList());
    }
}
