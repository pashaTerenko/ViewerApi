package com.terenko.viewerapi;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.terenko.viewerapi.API.API;
import com.terenko.viewerapi.API.ApiService;
import com.terenko.viewerapi.API.Responce;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class App extends Application {
    ApiService apiService;
    List<String> postIdList;
    int currentPost = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        apiService = new ApiService();
        postIdList = new ArrayList<>();

    }

    public void setCurrentPost(String currentPost) {

        this.currentPost = postIdList.indexOf(currentPost);
    }

    public ApiService getApiService() {
        return apiService;
    }

   public void refresh(List<String> list){
        list.forEach(c -> postIdList.add(c));
   }

    public String getCurrentPost() {
        return postIdList.get(currentPost);
    }

    public int nextPost() {
        if (postIdList.size() > 0)
            return currentPost = currentPost + 1 >= postIdList.size() ? 0 : currentPost + 1;
        return 0;
    }

    public int prevPost() {
        if (postIdList.size() > 0)
            return currentPost = currentPost - 1 < 0 ? postIdList.size()-1: currentPost -1;
        return 0;
    }
    public  boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public void refreshPosts() {
        if (isNetworkAvailable()) {
            getApiService().getApi().getPostId().enqueue(new Callback<List<String>>() {
                @Override
                public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                    refresh(response.body());

                }

                @Override
                public void onFailure(Call<List<String>> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    public void refreshPostsAndReload(MainActivity activity) {
        if (isNetworkAvailable()) {
            getApiService().getApi().getPostId().enqueue(new Callback<List<String>>() {
                @Override
                public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                    refresh(response.body());
                   activity.loadPost();
                }

                @Override
                public void onFailure(Call<List<String>> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

}
