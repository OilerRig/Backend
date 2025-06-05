package com.oilerrig.backend.service;

import com.oilerrig.backend.config.RabbitConfig;
import com.oilerrig.backend.data.saga.SagaMetadata;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class SagaService {


    @RabbitListener(queues = RabbitConfig.ORDER_SAGA_QUEUE)
    public void handleSaga(SagaMetadata metadata) {
        // TODO
    }



}
