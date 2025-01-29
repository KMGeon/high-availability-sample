package me.geon.event;

import lombok.Getter;
import me.geon.dataserializer.DataSerializer;


@Getter
public class Event<T extends EventPayload> {
    private Long eventId;
    private EventType type;
    private T payload;

    //
    public static Event<EventPayload> of(Long eventId, EventType type, EventPayload payload) {
        Event<EventPayload> event = new Event<>();
        event.eventId = eventId;
        event.type = type;
        event.payload = payload;
        return event;
    }

    // json으로 변경하여 카프카로 전달할 때
    public String toJson() {
        return DataSerializer.serialize(this);
    }

    // json을 받아서 이벤트로 받는 것을 의미한다.
    public static Event<EventPayload> fromJson(String json) {
        EventRaw eventRaw = DataSerializer.deserialize(json, EventRaw.class);
        if (eventRaw == null) {
            return null;
        }
        Event<EventPayload> event = new Event<>();
        event.eventId = eventRaw.getEventId();
        event.type = EventType.from(eventRaw.getType());
        event.payload = DataSerializer.deserialize(eventRaw.getPayload(), event.type.getPayloadClass());
        return event;
    }

    // 처음에 이벤트를 받을 때 타입이 달라서 일단 object로 받이 이후에 처리
    @Getter
    private static class EventRaw {
        private Long eventId;
        private String type;
        private Object payload;
    }
}
