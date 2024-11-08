package ru.zmaev.service;

import ru.zmaev.commonlib.model.dto.BuildMessage;

public interface RabbitMQProducerService {
    void sendMessage(BuildMessage message);
}
