package hu.listopad.socialnetworks.spring.web.controller;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class MySerializer extends JsonSerializer<String> {

    @Override
    public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {

        ObjectMapper mapper = new ObjectMapper();

        JsonNode node = mapper.readTree(value);
        String messageJson = mapper.writeValueAsString(node);
        jgen.writeRawValue(messageJson);
    }
}