package ru.zmaev.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.zmaev.commonlib.model.dto.BuildMessage;
import ru.zmaev.service.RabbitMQProducerService;

@Slf4j
@Service
@RequiredArgsConstructor
public class RabbitMQProducerServiceImpl implements RabbitMQProducerService {

    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.template.exchange.name}")
    private String exchangeName;
    @Value("${spring.rabbitmq.template.binding.routing-key}")
    private String routingKey;

    @Override
    public void sendMessage(BuildMessage message) {
        rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
        log.info("Message: {} sent to queue", message);
    }
}
