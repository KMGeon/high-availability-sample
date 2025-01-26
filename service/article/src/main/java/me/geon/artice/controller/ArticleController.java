package me.geon.artice.controller;

import lombok.RequiredArgsConstructor;
import me.geon.artice.ApiResponse;
import me.geon.artice.service.ArticleService;
import me.geon.artice.service.request.ArticleCreateRequest;
import me.geon.artice.service.request.ArticleUpdateRequest;
import me.geon.artice.service.response.ArticlePageResponse;
import me.geon.artice.service.response.ArticleResponse;
import me.geon.snowflake.Snowflake;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final Snowflake snowflake = new Snowflake();


    @GetMapping("/v1/articles/{articleId}")
    public ApiResponse<ArticleResponse> read(@PathVariable Long articleId) {
        return ApiResponse.success(articleService.read(articleId));
    }

    @GetMapping("/v1/articles")
    public ApiResponse<ArticlePageResponse> readAll(@RequestParam("boardId") Long boardId,
                                                    @RequestParam("page") Long page,
                                                    @RequestParam("pageSize") Long pageSize) {
        return ApiResponse.success(articleService.readAll(boardId, page, pageSize));
    }

    @GetMapping("/v1/articles/infinite-scroll")
    public ApiResponse<List<ArticleResponse>> readAllInfiniteScroll(@RequestParam("boardId") Long boardId,
                                                       @RequestParam("pageSize") Long pageSize,
                                                       @RequestParam(value = "lastArticleId", required = false) Long lastArticleId) {
        List<ArticleResponse> rtn = articleService.readAllInfiniteScroll(boardId, pageSize, lastArticleId);
        return ApiResponse.success(rtn);
    }

    @PostMapping("/v1/articles")
    public ApiResponse<ArticleResponse> create(@RequestBody ArticleCreateRequest request) {
        long id = snowflake.nextId();
        ArticleResponse rtn = articleService.create(request, id);
        return ApiResponse.success(rtn);
    }

    @PutMapping("/v1/articles/{articleId}")
    public ApiResponse<ArticleResponse> update(@PathVariable Long articleId, @RequestBody ArticleUpdateRequest request) {
        ArticleResponse rtn = articleService.update(articleId, request);
        return ApiResponse.success(rtn);
    }

    @DeleteMapping("/v1/articles/{articleId}")
    public ApiResponse<String> delete(@PathVariable Long articleId) {
        articleService.delete(articleId);
        return ApiResponse.SUCCESS;
    }
}
