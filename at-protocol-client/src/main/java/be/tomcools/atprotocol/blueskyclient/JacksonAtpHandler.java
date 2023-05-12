package be.tomcools.atprotocol.blueskyclient;

import be.tomcools.atprotocol.client.AtpJsonHandler;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.time.LocalDateTime;

public class JacksonAtpHandler implements AtpJsonHandler {
	private final ObjectMapper mapper = new ObjectMapper()
			.registerModule(new JavaTimeModule().addSerializer(LocalDateTime.class, new DateTimeSerializer()))
			.setSerializationInclusion(JsonInclude.Include.NON_NULL);


	@Override
	public <T> T fromJson(String input, Class<T> targetClass) {
		try {
			return mapper.readValue(input, targetClass);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Error while reading the JSON", e);
		}
	}
	@Override
	public <T> String toJson(T input) {
		try {
			return mapper.writeValueAsString(input);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Error while writing object to JSON", e);
		}
	}

	public static class DateTimeSerializer extends JsonSerializer<LocalDateTime> {
		@Override
		public void serialize(LocalDateTime dateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
				throws IOException {
			jsonGenerator.writeObject(dateTime.toString());
		}
	}

}
