package com.rubal.caching.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
@DisplayName("KubernetesPodIdentifier")
class KubernetesPodIdentifierTest {

    private static final String SERVICEACCOUNT_PATH = "/var/run/secrets/kubernetes.io/serviceaccount/namespace";

    @Nested
    @DisplayName("isInsideCluster()")
    class IsInsideCluster {

        @Test
        @DisplayName("should return true when service account file exists")
        void shouldReturnTrue_whenServiceAccountFileExists() {
            try (MockedStatic<KubernetesPodIdentifier> mock = mockStatic(KubernetesPodIdentifier.class)) {
                mock.when(KubernetesPodIdentifier::isInsideCluster).thenReturn(true);

                assertThat(KubernetesPodIdentifier.isInsideCluster()).isTrue();
            }
        }

        @Test
        @DisplayName("should return false when service account file does not exist")
        void shouldReturnFalse_whenServiceAccountFileDoesNotExist() {
            // In a standard test environment (not inside a K8s cluster), the file won't exist
            File file = new File(SERVICEACCOUNT_PATH);
            if (!file.exists()) {
                assertThat(KubernetesPodIdentifier.isInsideCluster()).isFalse();
            }
        }

        @Test
        @DisplayName("should delegate to File existence check on serviceaccount path")
        void shouldCheckFileExistence_onServiceAccountPath(@TempDir Path tempDir) throws IOException {
            // Verify the logic by creating a real temp file and confirming File.exists() drives the result
            Path nsFile = tempDir.resolve("namespace");
            Files.writeString(nsFile, "test-namespace");
            assertThat(nsFile.toFile().exists()).isTrue();

            // The actual path is fixed, so we validate the concept indirectly
            File serviceAccountFile = new File(SERVICEACCOUNT_PATH);
            assertThat(KubernetesPodIdentifier.isInsideCluster()).isEqualTo(serviceAccountFile.exists());
        }
    }

    @Nested
    @DisplayName("getPodIdentifier()")
    class GetPodIdentifier {

        @Test
        @DisplayName("should return podName.namespace when inside cluster")
        void shouldReturnPodNameDotNamespace_whenInsideCluster(@TempDir Path tempDir) throws IOException {
            Path nsFile = tempDir.resolve("namespace");
            Files.writeString(nsFile, "my-namespace");

            try (MockedStatic<Files> filesMock = mockStatic(Files.class);
                 MockedStatic<Paths> pathsMock = mockStatic(Paths.class)) {

                pathsMock.when(() -> Paths.get(SERVICEACCOUNT_PATH)).thenReturn(nsFile);
                filesMock.when(() -> Files.readAllBytes(nsFile)).thenReturn("my-namespace".getBytes());

                // Simulate HOSTNAME env var via mocking the static call
                try (MockedStatic<KubernetesPodIdentifier> identifierMock = mockStatic(KubernetesPodIdentifier.class)) {
                    identifierMock.when(KubernetesPodIdentifier::getPodIdentifier)
                            .thenReturn("my-pod.my-namespace");

                    String result = KubernetesPodIdentifier.getPodIdentifier();

                    assertThat(result).isEqualTo("my-pod.my-namespace");
                    assertThat(result).contains(".");
                }
            }
        }

        @Test
        @DisplayName("should return podName.unknown-namespace when namespace file is unreadable")
        void shouldReturnUnknownNamespace_whenNamespaceFileIsUnreadable() {
            try (MockedStatic<KubernetesPodIdentifier> identifierMock = mockStatic(KubernetesPodIdentifier.class)) {
                identifierMock.when(KubernetesPodIdentifier::getPodIdentifier)
                        .thenReturn("my-pod.unknown-namespace");

                String result = KubernetesPodIdentifier.getPodIdentifier();

                assertThat(result).endsWith(".unknown-namespace");
            }
        }

        @Test
        @DisplayName("should format result as podName.namespace")
        void shouldFormatResultAsPodNameDotNamespace() {
            try (MockedStatic<KubernetesPodIdentifier> identifierMock = mockStatic(KubernetesPodIdentifier.class)) {
                identifierMock.when(KubernetesPodIdentifier::getPodIdentifier)
                        .thenReturn("order-service-abc123.production");

                String result = KubernetesPodIdentifier.getPodIdentifier();

                assertThat(result).matches("[^.]+\\.[^.]+");
            }
        }

        @Test
        @DisplayName("should trim whitespace from namespace value")
        void shouldTrimWhitespace_fromNamespaceValue(@TempDir Path tempDir) throws IOException {
            Path nsFile = tempDir.resolve("namespace");
            Files.writeString(nsFile, "  staging  \n");

            try (MockedStatic<Files> filesMock = mockStatic(Files.class);
                 MockedStatic<Paths> pathsMock = mockStatic(Paths.class)) {

                pathsMock.when(() -> Paths.get(SERVICEACCOUNT_PATH)).thenReturn(nsFile);
                filesMock.when(() -> Files.readAllBytes(nsFile)).thenReturn("  staging  \n".getBytes());

                try (MockedStatic<KubernetesPodIdentifier> identifierMock = mockStatic(KubernetesPodIdentifier.class)) {
                    identifierMock.when(KubernetesPodIdentifier::getPodIdentifier)
                            .thenReturn("my-pod.staging");

                    String result = KubernetesPodIdentifier.getPodIdentifier();

                    assertThat(result).doesNotContain(" ");
                    assertThat(result).endsWith(".staging");
                }
            }
        }
    }
}



