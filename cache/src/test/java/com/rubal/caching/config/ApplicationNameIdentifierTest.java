package com.rubal.caching.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ApplicationNameIdentifier")
class ApplicationNameIdentifierTest {

    @Mock
    private Environment environment;

    @Nested
    @DisplayName("applicationName()")
    class ApplicationName {

        @Nested
        @DisplayName("when inside a Kubernetes cluster")
        class InsideKubernetesCluster {

            @Test
            @DisplayName("should return pod identifier from KubernetesPodIdentifier")
            void shouldReturnPodIdentifier_whenInsideCluster() {
                try (MockedStatic<KubernetesPodIdentifier> k8sMock = mockStatic(KubernetesPodIdentifier.class)) {
                    k8sMock.when(KubernetesPodIdentifier::isInsideCluster).thenReturn(true);
                    k8sMock.when(KubernetesPodIdentifier::getPodIdentifier).thenReturn("my-pod.my-namespace");

                    String result = ApplicationNameIdentifier.applicationName(environment);

                    assertThat(result).isEqualTo("my-pod.my-namespace");
                    k8sMock.verify(KubernetesPodIdentifier::getPodIdentifier);
                }
            }

            @Test
            @DisplayName("should not read spring environment properties when inside cluster")
            void shouldNotReadEnvironmentProperties_whenInsideCluster() {
                try (MockedStatic<KubernetesPodIdentifier> k8sMock = mockStatic(KubernetesPodIdentifier.class)) {
                    k8sMock.when(KubernetesPodIdentifier::isInsideCluster).thenReturn(true);
                    k8sMock.when(KubernetesPodIdentifier::getPodIdentifier).thenReturn("svc-pod.default");

                    ApplicationNameIdentifier.applicationName(environment);

                    // Environment should never be queried when inside a cluster
                    verify(environment, org.mockito.Mockito.never())
                            .getProperty(org.mockito.ArgumentMatchers.anyString());
                    verify(environment, org.mockito.Mockito.never())
                            .getProperty(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.anyString());
                }
            }
        }

        @Nested
        @DisplayName("when outside a Kubernetes cluster")
        class OutsideKubernetesCluster {

            @Test
            @DisplayName("should return spring.application.name when set")
            void shouldReturnApplicationName_whenPropertySet() {
                try (MockedStatic<KubernetesPodIdentifier> k8sMock = mockStatic(KubernetesPodIdentifier.class)) {
                    k8sMock.when(KubernetesPodIdentifier::isInsideCluster).thenReturn(false);
                    when(environment.getProperty("server.servlet.context-path", "/esf")).thenReturn("/my-service");
                    when(environment.getProperty("spring.application.name", "/my-service")).thenReturn("my-service");

                    String result = ApplicationNameIdentifier.applicationName(environment);

                    assertThat(result).isEqualTo("my-service");
                }
            }

            @Test
            @DisplayName("should fall back to context-path when spring.application.name is not set")
            void shouldFallBackToContextPath_whenApplicationNameNotSet() {
                try (MockedStatic<KubernetesPodIdentifier> k8sMock = mockStatic(KubernetesPodIdentifier.class)) {
                    k8sMock.when(KubernetesPodIdentifier::isInsideCluster).thenReturn(false);
                    when(environment.getProperty("server.servlet.context-path", "/esf")).thenReturn("/my-context");
                    when(environment.getProperty("spring.application.name", "/my-context")).thenReturn("/my-context");

                    String result = ApplicationNameIdentifier.applicationName(environment);

                    assertThat(result).isEqualTo("/my-context");
                }
            }

            @Test
            @DisplayName("should fall back to /esf when neither property is set")
            void shouldFallBackToDefaultEsf_whenNoPropertiesSet() {
                try (MockedStatic<KubernetesPodIdentifier> k8sMock = mockStatic(KubernetesPodIdentifier.class)) {
                    k8sMock.when(KubernetesPodIdentifier::isInsideCluster).thenReturn(false);
                    when(environment.getProperty("server.servlet.context-path", "/esf")).thenReturn("/esf");
                    when(environment.getProperty("spring.application.name", "/esf")).thenReturn("/esf");

                    String result = ApplicationNameIdentifier.applicationName(environment);

                    assertThat(result).isEqualTo("/esf");
                }
            }

            @Test
            @DisplayName("should prefer spring.application.name over context-path")
            void shouldPreferApplicationName_overContextPath() {
                try (MockedStatic<KubernetesPodIdentifier> k8sMock = mockStatic(KubernetesPodIdentifier.class)) {
                    k8sMock.when(KubernetesPodIdentifier::isInsideCluster).thenReturn(false);
                    when(environment.getProperty("server.servlet.context-path", "/esf")).thenReturn("/context-path");
                    when(environment.getProperty("spring.application.name", "/context-path")).thenReturn("explicit-app-name");

                    String result = ApplicationNameIdentifier.applicationName(environment);

                    assertThat(result).isEqualTo("explicit-app-name");
                    assertThat(result).doesNotContain("context-path");
                }
            }

            @Test
            @DisplayName("should not invoke KubernetesPodIdentifier.getPodIdentifier when outside cluster")
            void shouldNotInvokePodIdentifier_whenOutsideCluster() {
                try (MockedStatic<KubernetesPodIdentifier> k8sMock = mockStatic(KubernetesPodIdentifier.class)) {
                    k8sMock.when(KubernetesPodIdentifier::isInsideCluster).thenReturn(false);
                    when(environment.getProperty("server.servlet.context-path", "/esf")).thenReturn("/esf");
                    when(environment.getProperty("spring.application.name", "/esf")).thenReturn("/esf");

                    ApplicationNameIdentifier.applicationName(environment);

                    k8sMock.verify(KubernetesPodIdentifier::isInsideCluster);
                    k8sMock.verify(KubernetesPodIdentifier::getPodIdentifier,
                            org.mockito.Mockito.never());
                }
            }
        }
    }
}

