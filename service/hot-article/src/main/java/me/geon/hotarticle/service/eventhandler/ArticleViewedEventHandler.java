package me.geon.hotarticle.service.eventhandler;

import lombok.RequiredArgsConstructor;
import me.geon.event.Event;
import me.geon.event.EventType;
import me.geon.event.payload.ArticleViewedEventPayload;
import me.geon.hotarticle.repository.ArticleViewCountRepository;
import me.geon.hotarticle.utils.TimeCalculatorUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticleViewedEventHandler implements EventHandler<ArticleViewedEventPayload> {
    private final ArticleViewCountRepository articleViewCountRepository;

    @Override
    public void handle(Event<ArticleViewedEventPayload> event) {
        ArticleViewedEventPayload payload = event.getPayload();
        articleViewCountRepository.createOrUpdate(
                payload.getArticleId(),
                payload.getArticleViewCount(),
                TimeCalculatorUtils.calculateDurationToMidnight()
        );
    }

    @Override
    public boolean supports(Event<ArticleViewedEventPayload> event) {
        return EventType.ARTICLE_VIEWED == event.getType();
    }

    @Override
    public Long findArticleId(Event<ArticleViewedEventPayload> event) {
        return event.getPayload().getArticleId();
    }
}
