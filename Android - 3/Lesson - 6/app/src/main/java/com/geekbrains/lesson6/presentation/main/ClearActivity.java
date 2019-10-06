package com.geekbrains.lesson6.presentation.main;

import android.os.Bundle;

import com.geekbrains.lesson6.R;
import com.geekbrains.lesson6.data.db.AppDatabase;
import com.geekbrains.lesson6.data.db.DbProvider;
import com.geekbrains.lesson6.data.db.RealmDbImpl;
import com.geekbrains.lesson6.data.db.RoomDbImpl;
import com.geekbrains.lesson6.data.db.SugarDbImpl;
import com.geekbrains.lesson6.data.entity.ArticleRealmData;
import com.geekbrains.lesson6.data.entity.ArticleRoomData;
import com.geekbrains.lesson6.data.entity.ArticleSugarData;
import com.geekbrains.lesson6.data.network.Api;
import com.geekbrains.lesson6.data.network.RetrofitInit;
import com.geekbrains.lesson6.data.repository.ArticleRepositoryImpl;
import com.geekbrains.lesson6.domain.model.Article;
import com.geekbrains.lesson6.domain.repository.ArticleRepository;
import com.geekbrains.lesson6.domain.usecase.ArticleInteractor;
import com.orm.SchemaGenerator;
import com.orm.SugarContext;
import com.orm.SugarDb;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ClearActivity extends AppCompatActivity {

    private ArticleAdapter adapter;
    private ClearViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        initViewModel();
        initRecycleView();

        viewModel.articleLiveData.observe(this, data -> {
            adapter.setList(data);
        });
    }

    private void initViewModel() {
        Api api = RetrofitInit.newApiInstance();
        SugarContext.init(getApplicationContext());
        SchemaGenerator schemaGenerator = new SchemaGenerator(this);
        schemaGenerator.createDatabase(new SugarDb(this).getDB());
        DbProvider<ArticleRealmData, List<Article>> dbRealm = new RealmDbImpl();
        DbProvider<ArticleRoomData, List<Article>> dbRoom = new RoomDbImpl(AppDatabase.getInstance(this));
        DbProvider<ArticleSugarData, List<Article>> dbSugar = new SugarDbImpl();

        ArticleRepository repository = new ArticleRepositoryImpl(api, dbRealm, dbRoom, dbSugar);
        ArticleInteractor interactor = new ArticleInteractor(repository);

        viewModel = ViewModelProviders.of(this, new ClearViewModelFactory(interactor)).get(ClearViewModel.class);
        getLifecycle().addObserver(viewModel);
    }

    private void initRecycleView() {
        adapter = new ArticleAdapter();

        RecyclerView rv = findViewById(R.id.rv_articles);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SugarContext.terminate();
    }
}
