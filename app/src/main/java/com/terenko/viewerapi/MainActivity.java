package com.terenko.viewerapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.terenko.viewerapi.API.ApiService;
import com.terenko.viewerapi.API.Responce;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    protected static final String EXTRA_DATE_PAYLOAD = "TextViewActivity.EXTRA_DATE_PAYLOAD";
    protected static final String EXTRA_DATE_ID = "TextViewActivity.EXTRA_DATE_ID";
    App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle(getString(R.string.swipezone));
        app = (App) getApplication();

        if (this.getClass() == MainActivity.class)
            refreshPostsAndReload();
        else {
            app.setCurrentPost(getIntent().getStringExtra(EXTRA_DATE_ID));
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (!(this.getClass() == MainActivity.class))

            app.setCurrentPost(getIntent().getStringExtra(EXTRA_DATE_ID));
        }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.next:
                app.nextPost();
                loadPost();
                return false;
            case R.id.prev:
                app.prevPost();
                loadPost();
                return false;

                // Respond to the action bar's Up/Home button

        }
        return super.onOptionsItemSelected(item);
    }

   /* public void setListener() {
        findViewById(R.id.next).setOnClickListener(v -> {
            app.nextPost();
            loadPost();
        });
        findViewById(R.id.prev).setOnClickListener(v -> {
            app.prevPost();
            loadPost();
        });
    }*/

    /* public void swipeListener() {

                 getActionBarView().setOnTouchListener(new OnSwipeTouchListener(this) {
             @Override
             public void onSwipeLeft() {

                 app.nextPost();
                 loadPost();
             }

             @Override
             public void onSwipeRight() {
                 app.prevPost();
                 loadPost();
             }
         });
     }
 */
    public void callText(String payload) {
        TextActivity.start(this, payload,app.getCurrentPost());
    }

    public void callWeb(String payload) {

        WebViewActivity.start(this, payload,app.getCurrentPost());
    }

    public void loadPost() {
        try {
            if((getIntent().getStringExtra(EXTRA_DATE_ID).equals(app.getCurrentPost())))
                return;
        } catch (NullPointerException e) {

        }
        String id = app.getCurrentPost();
        app.getApiService().getApi().getPost(id).enqueue(new Callback<Responce>() {
            @Override
            public void onResponse(Call<Responce> call, Response<Responce> response) {
                Responce post = response.body();
                switch (post.getType()) {
                    case "text": {
                        callText(post.getPayload().getText());
                        break;
                    }

                    case "webpage": {
                        callWeb(post.getPayload().getUrl());

                        break;
                    }

                }
            }

            @Override
            public void onFailure(Call<Responce> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void refreshPosts() {
        if (app.isNetworkAvailable()) {
            app.getApiService().getApi().getPostId().enqueue(new Callback<List<String>>() {
                @Override
                public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                    app.refresh(response.body());

                }

                @Override
                public void onFailure(Call<List<String>> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    public void refreshPostsAndReload() {
        if (app.isNetworkAvailable()) {
            app.getApiService().getApi().getPostId().enqueue(new Callback<List<String>>() {
                @Override
                public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                    app.refresh(response.body());
                    loadPost();
                }

                @Override
                public void onFailure(Call<List<String>> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }


}