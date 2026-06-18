package com.rubal.caching.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

public class ApplicationNameIdentifier {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationNameIdentifier.class);

    public static String applicationName(Environment environment) {
        if (KubernetesPodIdentifier.isInsideCluster()) {
            logger.debug("Detected execution inside a Kubernetes Cluster.");
            return KubernetesPodIdentifier.getPodIdentifier();
        }
        String appContextPath = environment.getProperty("server.servlet.context-path", "/esf");
        String appName = environment.getProperty("spring.application.name", appContextPath);
        logger.trace("Application Name: {}", appName);
        return appName;
    }
}
