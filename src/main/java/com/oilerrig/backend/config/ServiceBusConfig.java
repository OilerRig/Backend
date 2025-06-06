package com.oilerrig.backend.config;

import com.azure.spring.messaging.implementation.annotation.EnableAzureMessaging;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableAzureMessaging
public class ServiceBusConfig {

    public static final String SAGA_QUEUE = "queue.sagas.live";
    public static final String WAIT_QUEUE = "queue.sagas.wait";
    public static final String LOG_QUEUE = "queue.logging";

}