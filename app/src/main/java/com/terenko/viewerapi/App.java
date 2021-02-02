package com.terenko.viewerapi;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.terenko.viewerapi.API.ApiService;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class App extends Application {
    ApiService apiService;
    MutableLiveData<List<String>> postIdList;
    int currentPost = 0;
    String currentPostID="";

    @Override
    public void onCreate() {
        super.onCreate();
        apiService = new ApiService();
        postIdList = new MutableLiveData<>();
        //назначения указателя на текущий пост при обновлении списка ID
        postIdList.observeForever(list -> {
            if(!currentPostID.equals(""))
        setCurrentPost();
        });
        update();
    }
    public String getActionBarString(){

        return currentPost+1+"/"+postIdList.getValue().size();
    }
    public void setCurrentPost() {
        if(postIdList.getValue()==null)
            return;
        int postidIndx= postIdList.getValue().indexOf(currentPostID);
        if(postidIndx!=-1)
        this.currentPost = postidIndx;
        else {
            this.currentPost = 0;
            this.currentPostID="";
        }
    }

    public ApiService getApiService() {
        return apiService;
    }



    public String getCurrentPost() {
        return postIdList.getValue().get(currentPost);
    }

    public int nextPost() {
        if(postIdList.getValue()==null)
            return 0;
        if (postIdList.getValue().size() > 0) {
             currentPost = currentPost + 1 >= postIdList.getValue().size() ? 0 : currentPost + 1;
            currentPostID = getCurrentPost();
            return currentPost;
        }
        return 0;
    }

    public int prevPost() {
        if(postIdList.getValue()==null)
            return 0;
        if (postIdList.getValue().size() > 0){
             currentPost = currentPost - 1 < 0 ? postIdList.getValue().size()-1: currentPost -1;
        currentPostID=getCurrentPost();
        return currentPost;
        }
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
                public void onResponse(@NotNull Call<List<String>> call,@NotNull Response<List<String>> response) {
                    if(response.body()!=null) {
                        postIdList.setValue(response.body());
                        currentPostID = getCurrentPost();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<List<String>> call, @NotNull Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    public void refreshPostsAndReload(MainActivity activity) {
        if (isNetworkAvailable()) {
            getApiService().getApi().getPostId().enqueue(new Callback<List<String>>() {
                @Override
                public void onResponse( @NotNull Call<List<String>> call,@NotNull Response<List<String>> response) {
                    if(response.body()!=null) {
                        postIdList.setValue(response.body());
                        currentPostID = getCurrentPost();
                        activity.loadPost();
                    }

                }

                @Override
                public void onFailure(@NotNull Call<List<String>> call,@NotNull Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    public String getCurrentPostID() {
        return currentPostID;
    }
    private void update(){
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleWithFixedDelay(() -> refreshPosts(), 0, 10, TimeUnit.SECONDS);
    }
}
