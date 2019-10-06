package com.geekbrains.lesson6.data.entity;

import com.geekbrains.lesson6.domain.model.Article;
import com.orm.SugarRecord;

public class ArticleSugarData extends SugarRecord {

    private String author;
    private String title;
    private String description;
    private String url;
    private String urlToImage;
    private String publishedAt;
    private String content;

    public ArticleSugarData(String author,
                            String title,
                            String description,
                            String url,
                            String urlToImage,
                            String publishedAt,
                            String content) {
        this.author = author;
        this.title = title;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
        this.content = content;
    }

    public static ArticleSugarData convertToSugarData(ArticleData item) {
        return new ArticleSugarData(
                item.author,
                item.title,
                item.description,
                item.url,
                item.urlToImage,
                item.publishedAt,
                item.content);
    }

    public static Article convertToEntity(ArticleSugarData item) {
        return new Article(
                item.author,
                item.title,
                item.description,
                item.url,
                item.urlToImage,
                item.publishedAt,
                item.content);
    }
}

