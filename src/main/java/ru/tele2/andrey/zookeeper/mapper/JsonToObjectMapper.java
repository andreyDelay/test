package ru.tele2.andrey.zookeeper.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JsonToObjectMapper {

    private final ObjectMapper mapper;

    public String objectToJson(Object o) {
        try {
            return mapper.writeValueAsString(o);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public <T> T jsonToObject(String json, Class<T> type) {
        try {
            return mapper.readValue(json, type);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
