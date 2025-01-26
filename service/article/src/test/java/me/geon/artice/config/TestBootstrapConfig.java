package me.geon.artice.config;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import me.geon.artice.domain.Article;
import me.geon.snowflake.Snowflake;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.transaction.support.TransactionTemplate;

@TestConfiguration
@RequiredArgsConstructor
public class TestBootstrapConfig {
    private final EntityManager entityManager;
    private final TransactionTemplate transactionTemplate;
    private final Snowflake snowflake;

    @PostConstruct
    public void initDB() {
        transactionTemplate.executeWithoutResult(status -> {
            for(int i = 0; i < 100; i++) {
                Article article = Article.create(
                        snowflake.nextId(),
                        "테스트 제목 " + i,
                        "테스트 내용 " + i,
                        1L,
                        1L
                );
                entityManager.persist(article);
            }
        });
    }
}