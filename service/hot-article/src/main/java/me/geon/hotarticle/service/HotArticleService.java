package me.geon.hotarticle.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.geon.event.Event;
import me.geon.event.EventPayload;
import me.geon.event.EventType;
import me.geon.hotarticle.client.ArticleClient;
import me.geon.hotarticle.repository.HotArticleListRepository;
import me.geon.hotarticle.service.eventhandler.EventHandler;
import me.geon.hotarticle.service.response.HotArticleResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class HotArticleService {
    private final ArticleClient articleClient;
    private final List<EventHandler<EventPayload>> eventHandlers;
    private final HotArticleScoreUpdater hotArticleScoreUpdater;
    private final HotArticleListRepository hotArticleListRepository;


    /**
     * 1. kafka로 이벤트를 받는다.
     */
    public void handleEvent(Event<EventPayload> event) {
        EventHandler<EventPayload> eventHandler = findEventHandler(event);
        if (eventHandler == null) {
            return;
        }


        if (isArticleCreatedOrDeleted(event)) { // 생성, 삭제
            eventHandler.handle(event);
        } else {
            hotArticleScoreUpdater.update(event, eventHandler);
        }
    }

    private EventHandler<EventPayload> findEventHandler(Event<EventPayload> event) {
         return eventHandlers.stream()
                .filter(eventHandler -> eventHandler.supports(event))
                .findAny()
                .orElse(null);
    }

    private boolean isArticleCreatedOrDeleted(Event<EventPayload> event) {
        return EventType.ARTICLE_CREATED == event.getType() || EventType.ARTICLE_DELETED == event.getType();
    }

    public List<HotArticleResponse> readAll(String dateStr) {
        return hotArticleListRepository.readAll(dateStr).stream()
                .map(articleClient::read)
                .filter(Objects::nonNull)
                .map(HotArticleResponse::from)
                .toList();
    }
}
