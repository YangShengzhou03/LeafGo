package com.leafgo.config;

import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer hibernateCustomizer() {
        return builder -> {
            Hibernate6Module hibernateModule = new Hibernate6Module();
            hibernateModule.configure(Hibernate6Module.Feature.FORCE_LAZY_LOADING, false);
            hibernateModule.configure(Hibernate6Module.Feature.SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS, true);
            builder.modules(hibernateModule);
        };
    }
}
