package com.oilerrig.backend.service;

import com.azure.spring.messaging.servicebus.core.ServiceBusTemplate;
import com.azure.spring.messaging.servicebus.implementation.core.annotation.ServiceBusListener;
import com.oilerrig.backend.config.ServiceBusConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    private final ServiceBusTemplate serviceBusTemplate;

    @Autowired
    TestService(ServiceBusTemplate serviceBusTemplate) {
        this.serviceBusTemplate = serviceBusTemplate;
    }

    public void pushToLogQueue(String message) {
        serviceBusTemplate.sendAsync(
                ServiceBusConfig.LOG_QUEUE,
                MessageBuilder.withPayload(message).build()
        ).subscribe();
    }

    @ServiceBusListener(destination = ServiceBusConfig.LOG_QUEUE)
    public void handleLogQueue(String payload) {
        System.out.println(payload);
    }


}
