package me.geon.like.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Table(name = "article_like",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "idx_article_id_user_id",
                        columnNames = {"article_id", "user_id"}
                )
        }
)
@Getter
@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleLike {
    @Id
    private Long articleLikeId;
    private Long articleId; // shard key
    private Long userId;
    private LocalDateTime createdAt;

    public static ArticleLike create(Long articleLikeId, Long articleId, Long userId) {
        ArticleLike articleLike = new ArticleLike();
        articleLike.articleLikeId = articleLikeId;
        articleLike.articleId = articleId;
        articleLike.userId = userId;
        articleLike.createdAt = LocalDateTime.now();
        return articleLike;
    }
}


/**
 create table article_like
 (
 article_like_id bigint   not null primary key,
 article_id      bigint   not null,
 user_id         bigint   not null,
 created_at      datetime not null
 );

 create unique index idx_article_id_user_id on article_like (article_id asc, user_id asc);

 */