package me.geon.artice.controller;

import me.geon.artice.ApiResponse;
import me.geon.artice.config.WebTestConfigure;
import me.geon.artice.data.DataInitializer;
import me.geon.artice.service.request.ArticleCreateRequest;
import me.geon.artice.service.response.ArticleResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleControllerTest extends WebTestConfigure {

    @BeforeEach
    void setUp() throws Exception {
        super.setup();
    }

    @Test
    @DisplayName("POST /v1/articles 게시글 생성 200 OK")
    public void 새로운_게시글을_생성한다() throws Exception {
        // given
        ArticleCreateRequest article = fixtureMonkey.giveMeBuilder(ArticleCreateRequest.class)
                .set("boardId", 1L)
                .set("writerId", 1L)
                .sample();
        // when
        ApiResponse<ArticleResponse> rtn = articleApiCaller.createArticle(article, 200);

        // then
        assertThat(rtn.getData())
                .extracting("title", "content", "boardId", "writerId")
                .containsExactly(article.getTitle(), article.getContent(), article.getBoardId(), article.getWriterId());
    }

    @Test
    @DisplayName("POST /v1/articles ArticleCreateRequest null")
    public void 새로운_게시글을_400에서() throws Exception {
        // given
        // when
        ApiResponse<ArticleResponse> rtn = articleApiCaller.createArticle(null, 400);

        // then
        Assertions.assertThat(rtn.getCode()).isEqualTo(400);
    }

}