package com.beeline.task.utils;

import android.content.Context;

import com.beeline.task.models.Article;
import com.beeline.task.models.Articles;
import com.beeline.task.retrofit.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RandomArticle {

    public boolean isDeveloperLimited = false;
    ApiService apiService;
    List<Article> articleList = new ArrayList<>();
    List<Article> allAvailableArticles = new ArrayList<>();
    ItemRequestListener itemRequestListener;
    private int remainData = 0;
    private int page = 1;


    public RandomArticle(ItemRequestListener itemRequestListener, Context context) {
        this.itemRequestListener = itemRequestListener;
        apiService = ServiceGenerator.createService(ApiService.class, Constants.AUTH_TOKEN);
        makeRequest("firstRender");

    }

    public List<Article> getNumberOfArticles(int number) {
        for (int i = 0; i < number; i++) {
            allAvailableArticles.add(articleList.get(i));
        }
        remainData = articleList.size() - number;
        return allAvailableArticles;
    }

    private void makeRequest(String type) {
        Call<Articles> apiCall = apiService.getArticles(this.page);
        apiCall.enqueue(new Callback<Articles>() {
            @Override
            public void onResponse(Call<Articles> call, Response<Articles> response) {
                if (response.isSuccessful()) {
                    articleList = response.body().getArticles();
                    setRemainData(articleList.size());
                    setPage(page + 1);
                    itemRequestListener.onItemReceived(type);
                } else {
                    setRemainData(0);
                }
            }

            @Override
            public void onFailure(Call<Articles> call, Throwable t) {
                setRemainData(0);
            }
        });
    }

    private void setRemainData(int remainData) {
        this.remainData = remainData;
    }

    private void setPage(int page) {
        this.page = page;
    }

    public List<Article> getAllAvailableArticles() {
        return allAvailableArticles;
    }

    public List<Article> getUpdatedArticle() {
        if (remainData > 0) {
            return getData();
        } else if (this.page > 5 && remainData == 0) {
            isDeveloperLimited = true;
        } else if (remainData == 0) {
            makeRequest("swipe");
        }
        return null;
    }

    private List<Article> getData() {
        int count = 0;
        int i = articleList.size() - remainData;
        List<Article> mArticles = new ArrayList<>();
        while (count < 10 && remainData > 0) {
            mArticles.add(articleList.get(i));
            count++;
            i++;
            remainData--;
        }
        mArticles.addAll(allAvailableArticles);
        allAvailableArticles = mArticles;
        return allAvailableArticles;
    }


    public List<Article> getBottomArticle() {
        if (remainData > 0) {
            return getBottomData();
        } else if (this.page > 5 && remainData == 0) {
            isDeveloperLimited = true;
        } else if (remainData == 0) {
            makeRequest("bottomReached");
        }
        return null;
    }

    private List<Article> getBottomData() {
        int count = 0;
        int i = articleList.size() - remainData;
        while (count < 10 && remainData > 0) {
            allAvailableArticles.add(articleList.get(i));
            count++;
            i++;
            remainData--;
        }
        return allAvailableArticles;
    }

}
