package com.rubal.caching.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class KubernetesPodIdentifier {

    private static final String SERVICEACCOUNT_PATH = "/var/run/secrets/kubernetes.io/serviceaccount/namespace";
    private static final Logger logger = LoggerFactory.getLogger(KubernetesPodIdentifier.class);

    public static boolean isInsideCluster() {
        return new File(SERVICEACCOUNT_PATH).exists();
    }

    public static String getPodIdentifier() {
        String podName = System.getenv("HOSTNAME");
        try {
            String namespace = new String(Files.readAllBytes(Paths.get(SERVICEACCOUNT_PATH))).trim();
            logger.trace("Current Namespace: {}", namespace);
            logger.trace("Current Pod Name: {}", podName);
            return "%s.%s".formatted(podName, namespace);
        } catch (IOException e) {
            logger.debug("Error reading Kubernetes service account namespace: {}", e.getMessage());
        }
        return "%s.unknown-namespace".formatted(podName);
    }

}
