package combase.pubsubpublisher.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MyObjectMapper {

    private static final Logger LOG = LoggerFactory.getLogger(MyObjectMapper.class);


    private final ObjectMapper customObjectMapper = new ObjectMapper();

    private final ObjectMapper defaultObjectMapper = new ObjectMapper();

    public MyObjectMapper() {
        customObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        customObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        customObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        customObjectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        customObjectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        customObjectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        customObjectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        customObjectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        customObjectMapper.registerModule(new JavaTimeModule());
    }

    public ObjectMapper getCustomObjectMapper() {
        return customObjectMapper;
    }

    public ObjectMapper getDefaultObjectMapper() {
        return defaultObjectMapper;
    }

    public String stringify(Object o) {
        try {
            return customObjectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            LOG.error(e.toString(), e);
        }
        return "failed object to json conversion";
    }
}
