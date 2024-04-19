package org.araa.infrastructure.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

public class CustomRedisSerializer extends GenericJackson2JsonRedisSerializer {
    private final ClassLoader classLoader;

    public CustomRedisSerializer(ClassLoader classLoader) {
        super();
        this.classLoader = classLoader;
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setTypeFactory(TypeFactory.defaultInstance().withClassLoader(classLoader));
            return objectMapper.readValue(bytes, Object.class);
        } catch (Exception e) {
            throw new SerializationException("Error deserializing bytes", e);
        }
    }
}
