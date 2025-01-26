package me.geon.artice.service.request;

import lombok.*;


@Getter
public class ArticleCreateRequest {
    private String title;
    private String content;
    private Long writerId;
    private Long boardId;

    @Override
    public String toString() {
        return "ArticleCreateRequest{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", writerId=" + writerId +
                ", boardId=" + boardId +
                '}';
    }
}