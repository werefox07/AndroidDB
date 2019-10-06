package com.geekbrains.lesson6.data.repository;

import android.os.Build;

import com.geekbrains.lesson6.data.db.DbProvider;
import com.geekbrains.lesson6.data.entity.ArticleData;
import com.geekbrains.lesson6.data.entity.ArticleRealmData;
import com.geekbrains.lesson6.data.entity.ArticleRoomData;
import com.geekbrains.lesson6.data.entity.ArticleSugarData;
import com.geekbrains.lesson6.data.network.Api;
import com.geekbrains.lesson6.domain.model.Article;
import com.geekbrains.lesson6.domain.repository.ArticleRepository;

import java.util.List;
import java.util.stream.Collectors;

import androidx.annotation.RequiresApi;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class ArticleRepositoryImpl implements ArticleRepository {

    private Api api;
    private DbProvider<ArticleRealmData, List<Article>> dbRealm;
    private DbProvider<ArticleRoomData, List<Article>> dbRoom;
    private DbProvider<ArticleSugarData, List<Article>> dbSugar;

    public ArticleRepositoryImpl(Api api,
                                 DbProvider<ArticleRealmData, List<Article>> dbRealm,
                                 DbProvider<ArticleRoomData, List<Article>> dbRoom,
                                 DbProvider<ArticleSugarData, List<Article>> dbSugar
    ) {
        this.api = api;
        this.dbRealm = dbRealm;
        this.dbRoom = dbRoom;
        this.dbSugar = dbSugar;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Single<List<Article>> getArticles() {
        return api.getArticles()
                .subscribeOn(Schedulers.io())
                .doOnSuccess(consumer -> {
                            for (ArticleData item : consumer.articles) {
                                dbRealm.insert(ArticleRealmData.convertToRealmData(item));
                            }
                        }
                )
                .doOnSuccess(consumer -> {
                            for (ArticleData item : consumer.articles) {
                                dbRoom.insert(ArticleRoomData.convertToRoomData(item));
                            }
                        }
                )
                .doOnSuccess(consumer -> {
                    for (ArticleData item : consumer.articles) {
                        dbSugar.insert(ArticleSugarData.convertToSugarData(item));
                        }
                    }
                )
                .map(response ->
                        response.articles.stream()
                                .map(articleData -> ArticleData.convertToEntity(articleData))
                                .collect(Collectors.toList())
                );
    }
}
