package ru.zmaev.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import ru.zmaev.commonlib.model.dto.BuildMessage;
import ru.zmaev.service.impl.RabbitMQProducerServiceImpl;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RabbitMQServiceTests {
    @Mock
    private RabbitTemplate rabbitTemplate;

    private RabbitMQProducerServiceImpl rabbitMQProducerService;

    @BeforeEach
    public void setUp() {
        rabbitMQProducerService = new RabbitMQProducerServiceImpl(rabbitTemplate);
    }

    @Test
    void testSendMessage() {
        BuildMessage message = new BuildMessage("uuid", 1L, "name", "description");
        rabbitMQProducerService.sendMessage(message);
        verify(rabbitTemplate, times(1)).convertAndSend(null, null, message);
    }
}
