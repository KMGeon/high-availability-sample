package me.geon.hotarticle.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.geon.event.Event;
import me.geon.event.EventPayload;
import me.geon.event.EventType;
import me.geon.hotarticle.service.HotArticleService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class HotArticleEventConsumer {
    private final HotArticleService hotArticleService;

    @KafkaListener(topics = {
            EventType.Topic.GEON_BOARD_VIEW,
            EventType.Topic.GEON_BOARD_LIKE,
            EventType.Topic.GEON_BOARD_COMMENT,
            EventType.Topic.GEON_BOARD_ARTICLE
    })
    public void listen(String message, Acknowledgment ack) {
        log.info("[HotArticleEventConsumer.listen] received message={}", message);
        Event<EventPayload> event = Event.fromJson(message);
        if (event != null) {
            hotArticleService.handleEvent(event);
        }
        ack.acknowledge();
    }
}
