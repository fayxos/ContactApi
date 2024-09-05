package com.projects.contact_api.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;

import java.util.List;
import java.util.Map;

public class HashMapConverter implements AttributeConverter<Map<String, List<String>>, String> {
    @Override
    public String convertToDatabaseColumn(Map<String, List<String>> map) {
        ObjectMapper objectMapper = new ObjectMapper();
        String dataJson;
        try {
            dataJson = objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return  dataJson;
    }

    @Override
    public Map<String, List<String>> convertToEntityAttribute(String dbData) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, List<String>> map;
        try {
            map = objectMapper.readValue(dbData, new TypeReference<Map<String, List<String>>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return  map;
    }
}
