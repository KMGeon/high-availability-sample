package me.geon.outboxmessagerelay;

import lombok.RequiredArgsConstructor;
import me.geon.event.Event;
import me.geon.event.EventPayload;
import me.geon.event.EventType;
import me.geon.snowflake.Snowflake;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OutboxEventPublisher {
    private final Snowflake outboxIdSnowflake = new Snowflake();
    private final Snowflake eventIdSnowflake = new Snowflake();
    private final ApplicationEventPublisher applicationEventPublisher;

    public void publish(EventType type, EventPayload payload) {
        Outbox outbox = Outbox.create(
                outboxIdSnowflake.nextId(),
                type,
                Event.of(
                        eventIdSnowflake.nextId(), type, payload
                ).toJson()
        );
        System.out.println("=======================OutboxEventPublisher.publish============================");

        applicationEventPublisher.publishEvent(OutboxEvent.of(outbox));
    }
}
