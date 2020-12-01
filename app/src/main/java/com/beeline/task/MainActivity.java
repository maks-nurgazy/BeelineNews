package com.beeline.task;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.beeline.task.adapter.ArticleAdapter;
import com.beeline.task.models.Article;
import com.beeline.task.utils.Constants;
import com.beeline.task.utils.ItemClickListener;
import com.beeline.task.utils.ItemRequestListener;
import com.beeline.task.utils.RandomArticle;

import java.util.List;


public class MainActivity extends AppCompatActivity implements ItemClickListener, ItemRequestListener {


    private RecyclerView articlesRecyclerView;
    private ArticleAdapter articleAdapter;
    private SwipeRefreshLayout swipeContainer;
    private RandomArticle randomArticle;

    public MainActivity() {
        randomArticle = new RandomArticle(this, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeContainer = findViewById(R.id.swipeContainer);


        initRecyclerView();

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showData("swipe");
                swipeContainer.setRefreshing(false);
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }


    private void initRecyclerView() {
        articlesRecyclerView = findViewById(R.id.articlesRecyclerView);
        articlesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        articleAdapter = new ArticleAdapter(this);
        articlesRecyclerView.setAdapter(articleAdapter);
        articlesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    showData("bottomReached");
                }
            }
        });
    }

    @Override
    public void onItemClicked(int position) {
        Intent intent = new Intent(this, ClickedArticleActivity.class);
        intent.putExtra(Constants.CLICKED_ARTICLE, randomArticle.getAllAvailableArticles().get(position));
        startActivity(intent);
    }

    @Override
    public void onItemReceived(String type) {
        showData(type);
    }

    private void showData(String type) {
        switch (type) {
            case "swipe":
                List<Article> updatedArticle = randomArticle.getUpdatedArticle();
                if (updatedArticle != null) {
                    articleAdapter.setItems(updatedArticle);
                } else {
                    if (randomArticle.isDeveloperLimited)
                        Toast.makeText(this, R.string.upgrade_to_paid_plan, Toast.LENGTH_SHORT).show();
                }
                break;
            case "bottomReached":
                List<Article> bottomArticle = randomArticle.getBottomArticle();
                if (bottomArticle != null) {
                    articleAdapter.setItems(bottomArticle);
                } else {
                    if (randomArticle.isDeveloperLimited)
                        Toast.makeText(this, R.string.upgrade_to_paid_plan, Toast.LENGTH_SHORT).show();
                }
                break;
            case "firstRender":
                List<Article> first10Article = randomArticle.getNumberOfArticles(10);
                articleAdapter.setItems(first10Article);
                break;
        }
    }
}