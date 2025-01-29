package me.geon.hotarticle.service.eventhandler;


import me.geon.event.Event;
import me.geon.event.EventPayload;

public interface EventHandler<T extends EventPayload> {
    void handle(Event<T> event);
    boolean supports(Event<T> event);
    Long findArticleId(Event<T> event);
}
