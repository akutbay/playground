package de.playground;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.google.common.collect.Lists;

@Configuration
public class ContextConfiguration {

  @Bean
  public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
    Jackson2ObjectMapperBuilder objectMapperBuilder = new Jackson2ObjectMapperBuilder() {

      @Override
      public void configure(ObjectMapper objectMapper) {
        super.configure(objectMapper);
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
      }
    };
    objectMapperBuilder.serializationInclusion(JsonInclude.Include.NON_NULL);
    objectMapperBuilder.serializationInclusion(JsonInclude.Include.NON_ABSENT);

    objectMapperBuilder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
        SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS);

    objectMapperBuilder.modules(new JavaTimeModule(), new Jdk8Module(), new ParameterNamesModule());

    return objectMapperBuilder;
  }

  @Bean
  public MongoConverter mongoConverter(MongoDbFactory mongoDbFactory) {
    MappingMongoConverter mappingMongoConverter =
        new MappingMongoConverter(new DefaultDbRefResolver(mongoDbFactory), new MongoMappingContext());
    mappingMongoConverter
        .setCustomConversions(new CustomConversions(Lists.newArrayList(new OptionalStringConverter())));

    return mappingMongoConverter;
  }


  private class OptionalStringConverter extends OptionalWriteConverter<String> {

  }

  private class OptionalWriteConverter<T> implements Converter<Optional<T>, T> {

    public T convert(Optional<T> source) {
      return source.isPresent() ? source.get() : null;
    }

  }

}
