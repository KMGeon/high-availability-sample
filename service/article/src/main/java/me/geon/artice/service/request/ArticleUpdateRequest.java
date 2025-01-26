package me.geon.artice.service.request;

import lombok.*;

@Getter
public class ArticleUpdateRequest {
    private String title;
    private String content;

    @Override
    public String toString() {
        return "ArticleUpdateRequest{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}