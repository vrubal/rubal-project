package com.rubal.caching.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FilterPreProcessor {

    private static final Logger logger = LoggerFactory.getLogger(FilterPreProcessor.class);

    private final Properties properties;

    public FilterPreProcessor() {
        ClassPathResource resource = new ClassPathResource("filter.properties");
        this.properties = new Properties();
        InputStream inputStream = null;
        try {
            inputStream = resource.getInputStream();
            properties.load(inputStream);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public String getProperty(FilterType version) {
        return properties.getProperty(version.value());
    }

    public enum FilterType {
        VERSION("version");

        private final String value;

        FilterType(String version) {
            this.value = version;
        }

        public String value() {
            return value;
        }
    }
}
