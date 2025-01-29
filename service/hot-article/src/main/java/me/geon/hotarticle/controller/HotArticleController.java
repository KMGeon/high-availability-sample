package me.geon.hotarticle.controller;

import lombok.RequiredArgsConstructor;
import me.geon.hotarticle.service.HotArticleService;
import me.geon.hotarticle.service.response.HotArticleResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class HotArticleController {
    private final HotArticleService articleService;

    private final HotArticleService hotArticleService;

    @GetMapping("/v1/hot-articles/articles/date/{dateStr}")
    public List<HotArticleResponse> readAll(@PathVariable("dateStr") String dateStr) {
        return hotArticleService.readAll(dateStr);
    }
}
