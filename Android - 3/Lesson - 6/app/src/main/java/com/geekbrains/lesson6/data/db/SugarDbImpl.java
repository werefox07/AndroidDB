package com.geekbrains.lesson6.data.db;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.geekbrains.lesson6.data.entity.ArticleSugarData;
import com.geekbrains.lesson6.domain.model.Article;

import java.util.ArrayList;
import java.util.List;

public class SugarDbImpl implements DbProvider<ArticleSugarData, List<Article>> {

    @Override
    public void insert(ArticleSugarData data) {
        data.save();
    }

    @Override
    public void update(ArticleSugarData data) {
        data.update();
    }

    @Override
    public void delete(ArticleSugarData data) {
        data.delete();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public List<Article> select() {
        List<ArticleSugarData> sugarDataList = ArticleSugarData.listAll(ArticleSugarData.class);
        List<Article> articleList = new ArrayList<>();

        for (ArticleSugarData articleSugarData: sugarDataList) {
            articleList.add(ArticleSugarData.convertToEntity(articleSugarData));
        }

        return articleList;
    }
}
