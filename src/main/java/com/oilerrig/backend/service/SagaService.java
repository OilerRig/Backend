package com.oilerrig.backend.service;

import com.oilerrig.backend.config.RabbitConfig;
import com.oilerrig.backend.data.saga.Saga;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class SagaService {


    @RabbitListener(queues = RabbitConfig.ORDER_SAGA_QUEUE)
    public void handleSaga(Saga saga) {
        // TODO

        // logic generally should be
        // 1. recv saga handle, check if it expired, and cancel it and mark order as failed
        // 2. if it hasn't attempt saga steps
        // 3. if any fails, revert all previous ones, and send saga back into queue
        // 4. if all succeed, mark order as successful
    }



}
