package me.geon.like.controller;

import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;


class ArticleLikeControllerTest {

    private static final String URL = "http://localhost:9002";
    RestClient restClient = RestClient.create();

    @Test
    public void likeAndUnlikeTest() throws Exception{
        Long articleId = 9999L;

        like(articleId, 1L);
        like(articleId, 2L);
        like(articleId, 3L);
    }

    void like(Long articleId, Long userId){
        RestClient.ResponseSpec retrieve = restClient.post()
                .uri("/v1/articles-likes/articles/{articleId}/users/{userId}", articleId, userId)
                .retrieve();
    }
}