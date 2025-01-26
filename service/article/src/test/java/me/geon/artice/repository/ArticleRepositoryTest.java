package me.geon.artice.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import me.geon.artice.config.AppConfig;
import me.geon.artice.config.TestBootstrapConfig;
import me.geon.artice.domain.Article;
import me.geon.snowflake.Snowflake;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@Import({Snowflake.class, AppConfig.class, TestBootstrapConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class ArticleRepositoryTest {

    @PersistenceContext
    EntityManager entityManager;


    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ObjectMapper objectMapper;

    private final Snowflake snowflake = new Snowflake();
    private static final Long LIMIT_SIZE = 10L;

    @Test
    public void 게시물_생성() throws Exception {
        // given
        long nextId = snowflake.nextId();
        Article article = Article.create(nextId, "제목", "내용", 1L, 1L);

        // when
        Article savedArticle = articleRepository.save(article);

        // then
        Assertions.assertThat(savedArticle)
                .extracting("articleId", "title", "content", "boardId", "writerId")
                .containsExactly(nextId, "제목", "내용", 1L, 1L);
    }

    @Test
    @DisplayName("사이즈 10개 기준의 1페이지")
    public void 페이징_쿼리() throws Exception{
        // given

        // when
        List<Article> rtn = articleRepository.findAll(1L, 10L, LIMIT_SIZE);

        System.out.println("rtn = " + rtn);
        // then
        assertAll(
                () -> Assertions.assertThat(rtn.size()).isEqualTo(10),
                () -> Assertions.assertThat(rtn).extracting(Article::getBoardId).contains(1L),
                () -> Assertions.assertThat(rtn).extracting(Article::getTitle).contains("테스트 제목 88"),
                () -> Assertions.assertThat(rtn).extracting(Article::getContent).contains("테스트 내용 88"),
                () -> Assertions.assertThat(rtn).extracting(Article::getWriterId).contains(1L)
        );
    }

    @Test
    public void 페이징_개수() throws Exception{
        // given

        // when
        Long count = articleRepository.count(1L, LIMIT_SIZE);

        // then
        Assertions.assertThat(count).isEqualTo(LIMIT_SIZE);
    }

    @Test
    public void 무한스크롤_처음_페이지() throws Exception{
        // given

        // when
        List<Article> rtn = articleRepository.findAllInfiniteScroll(1L, LIMIT_SIZE);

        // then
        Assertions.assertThat(rtn).satisfies(v1-> {
           assertAll(
                   () -> Assertions.assertThat(v1).extracting(Article::getTitle).contains("테스트 제목 98"),
                   () -> Assertions.assertThat(v1).extracting(Article::getContent).contains("테스트 내용 98"),
                   () -> Assertions.assertThat(v1).extracting(Article::getBoardId).contains(1L),
                   () -> Assertions.assertThat(v1).extracting(Article::getWriterId).contains(1L),
                   () -> Assertions.assertThat(v1).extracting(Article::getCreatedAt).isNotNull(),
                   () -> Assertions.assertThat(v1).extracting(Article::getModifiedAt).isNotNull(),
                   () -> Assertions.assertThat(v1).extracting(Article::getArticleId).isNotNull()
           );
        });
    }

    @Test
    public void 무한스크롤_중간_페이지() throws Exception {
        // given
        List<Article> firstPage = articleRepository.findAllInfiniteScroll(1L, LIMIT_SIZE);
        Long lastArticleId = firstPage.get(firstPage.size()-1).getArticleId();

        // when
        List<Article> rtn = articleRepository.findAllInfiniteScroll(1L, LIMIT_SIZE, lastArticleId);

        // then
        Assertions.assertThat(rtn).satisfies(v1-> {
            assertAll(
                    () -> Assertions.assertThat(v1).extracting(Article::getTitle).contains("테스트 제목 88"),
                    () -> Assertions.assertThat(v1).extracting(Article::getContent).contains("테스트 내용 88"),
                    () -> Assertions.assertThat(v1).extracting(Article::getBoardId).contains(1L),
                    () -> Assertions.assertThat(v1).extracting(Article::getWriterId).contains(1L),
                    () -> Assertions.assertThat(v1).extracting(Article::getCreatedAt).isNotNull(),
                    () -> Assertions.assertThat(v1).extracting(Article::getModifiedAt).isNotNull(),
                    () -> Assertions.assertThat(v1).extracting(Article::getArticleId).isNotNull()
            );
        });
    }

}