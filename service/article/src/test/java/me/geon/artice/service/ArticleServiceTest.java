package me.geon.artice.service;

import com.navercorp.fixturemonkey.FixtureMonkey;
import jakarta.persistence.EntityManager;
import me.geon.artice.config.TestBootstrapConfig;
import me.geon.artice.config.TestConfig;
import me.geon.artice.domain.Article;
import me.geon.artice.repository.ArticleRepository;
import me.geon.artice.service.request.ArticleCreateRequest;
import me.geon.artice.service.request.ArticleUpdateRequest;
import me.geon.artice.service.response.ArticlePageResponse;
import me.geon.artice.service.response.ArticleResponse;
import me.geon.snowflake.Snowflake;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;

@Import({TestConfig.class, Snowflake.class, TestBootstrapConfig.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class ArticleServiceTest {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private FixtureMonkey fixtureMonkey;

    private static final Long SNAKE_ID = 141530433720008707L;

    @Test
    public void 게시글_생성() throws Exception {
        // given
        ArticleCreateRequest article = fixtureMonkey.giveMeBuilder(ArticleCreateRequest.class)
                .set("writerId", 1L)
                .set("boardId", 1L)
                .sample();
        // when
        ArticleResponse rtn = articleService.create(article, SNAKE_ID);

        // then
        assertThat(rtn)
                .extracting("articleId", "title", "content", "boardId", "writerId")
                .containsExactly(SNAKE_ID, article.getTitle(), article.getContent(), article.getBoardId(), article.getWriterId());
    }

    @Test
    public void 게시글_업데이트() throws Exception {
        // given
        ArticleCreateRequest article = fixtureMonkey.giveMeBuilder(ArticleCreateRequest.class)
                .set("writerId", 1L)
                .set("boardId", 1L)
                .sample();
        ArticleResponse remainRtn = articleService.create(article, SNAKE_ID);

        // when
        ArticleUpdateRequest updateRequest = fixtureMonkey.giveMeBuilder(ArticleUpdateRequest.class)
                .set("title", "수정된 제목")
                .set("content", "수정된 내용")
                .sample();

        ArticleResponse rtn = articleService.update(SNAKE_ID, updateRequest);

        // then
        assertThat(remainRtn.getTitle()).isNotEqualTo("수정된 제목");
        assertThat(remainRtn.getContent()).isNotEqualTo("수정된 내용");
        assertThat(rtn.getTitle()).isEqualTo("수정된 제목");
        assertThat(rtn.getContent()).isEqualTo("수정된 내용");
    }

    @Test
    public void 단일_게시글_조회() throws Exception {
        // given
        ArticleResponse createArticle = creteArticle();
        // when
        ArticleResponse rtn = articleService.read(SNAKE_ID);

        // then
        assertThat(rtn)
                .extracting("articleId", "title", "content", "boardId", "writerId")
                .containsExactly(SNAKE_ID, createArticle.getTitle(), createArticle.getContent(), createArticle.getBoardId(), createArticle.getWriterId());
    }

    @Test
    public void 단일_게시글_삭제() throws Exception {
        // given
        ArticleResponse createArticle = creteArticle();
        // when
        articleService.delete(SNAKE_ID);

        // then
        Assertions.assertThatThrownBy(() -> articleService.read(SNAKE_ID))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("Article with id '141530433720008707' not found");
    }


    ArticleResponse creteArticle() {
        ArticleCreateRequest article = fixtureMonkey.giveMeBuilder(ArticleCreateRequest.class)
                .set("writerId", 1L)
                .set("boardId", 1L)
                .sample();
        // when
        return articleService.create(article, SNAKE_ID);
    }

    @Test
    public void 페이징_서비스_조회() throws Exception {
        // given/when
        ArticlePageResponse rtn = articleService.readAll(1L, 1L, 10L);
        System.out.println("rtn = " + rtn);

        // then
        assertAll(
                () -> assertThat(rtn.getArticles().size()).isEqualTo(10),
                () -> assertThat(rtn.getArticleCount()).isEqualTo(100),
                () -> assertThat(rtn.getArticles())
                        .extracting("title", "content", "boardId", "writerId")
                        .containsExactly(
                                tuple("테스트 제목 99", "테스트 내용 99", 1L, 1L),
                                tuple("테스트 제목 98", "테스트 내용 98", 1L, 1L),
                                tuple("테스트 제목 97", "테스트 내용 97", 1L, 1L),
                                tuple("테스트 제목 96", "테스트 내용 96", 1L, 1L),
                                tuple("테스트 제목 95", "테스트 내용 95", 1L, 1L),
                                tuple("테스트 제목 94", "테스트 내용 94", 1L, 1L),
                                tuple("테스트 제목 93", "테스트 내용 93", 1L, 1L),
                                tuple("테스트 제목 92", "테스트 내용 92", 1L, 1L),
                                tuple("테스트 제목 91", "테스트 내용 91", 1L, 1L),
                                tuple("테스트 제목 90", "테스트 내용 90", 1L, 1L)
                        )
        );
    }

    @Test
    void 무한스크롤_처음_페이지() {
        // given/when
        List<ArticleResponse> responses = articleService.readAllInfiniteScroll(1L, 10L, null);

        // then
        assertThat(responses)
                .hasSize(10)
                .extracting("title", "content", "boardId", "writerId")
                .containsExactly(
                        tuple("테스트 제목 99", "테스트 내용 99", 1L, 1L),
                        tuple("테스트 제목 98", "테스트 내용 98", 1L, 1L),
                        tuple("테스트 제목 97", "테스트 내용 97", 1L, 1L),
                        tuple("테스트 제목 96", "테스트 내용 96", 1L, 1L),
                        tuple("테스트 제목 95", "테스트 내용 95", 1L, 1L),
                        tuple("테스트 제목 94", "테스트 내용 94", 1L, 1L),
                        tuple("테스트 제목 93", "테스트 내용 93", 1L, 1L),
                        tuple("테스트 제목 92", "테스트 내용 92", 1L, 1L),
                        tuple("테스트 제목 91", "테스트 내용 91", 1L, 1L),
                        tuple("테스트 제목 90", "테스트 내용 90", 1L, 1L)
                );
    }

    @Test
    void 무한스크롤_다음_페이지() {
        // given
        List<ArticleResponse> firstPage = articleService.readAllInfiniteScroll(1L, 10L, null);
        Long lastArticleId = firstPage.get(firstPage.size()-1).getArticleId();

        // when
        List<ArticleResponse> responses = articleService.readAllInfiniteScroll(1L, 10L, lastArticleId);

        // then
        assertThat(responses)
                .hasSize(10)
                .extracting("title", "content", "boardId", "writerId")
                .containsExactly(
                        tuple("테스트 제목 89", "테스트 내용 89", 1L, 1L),
                        tuple("테스트 제목 88", "테스트 내용 88", 1L, 1L),
                        tuple("테스트 제목 87", "테스트 내용 87", 1L, 1L),
                        tuple("테스트 제목 86", "테스트 내용 86", 1L, 1L),
                        tuple("테스트 제목 85", "테스트 내용 85", 1L, 1L),
                        tuple("테스트 제목 84", "테스트 내용 84", 1L, 1L),
                        tuple("테스트 제목 83", "테스트 내용 83", 1L, 1L),
                        tuple("테스트 제목 82", "테스트 내용 82", 1L, 1L),
                        tuple("테스트 제목 81", "테스트 내용 81", 1L, 1L),
                        tuple("테스트 제목 80", "테스트 내용 80", 1L, 1L)
                );
    }
}
