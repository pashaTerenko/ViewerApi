package com.terenko.viewerapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.fragment.app.*;

import com.terenko.viewerapi.API.Responce;
import com.terenko.viewerapi.Fragments.FragmentText;
import com.terenko.viewerapi.Fragments.FragmentWeb;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    App app;
    String postID;
    ProgressBar loadingProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadingProgressBar = findViewById(R.id.loading);
        app = (App) getApplication();

        app.refreshPostsAndReload(this);


    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
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

    //для  других типов данных  создаем фрагмент и добавляем тип в setView
    public void setView(Responce data) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        for (Fragment fragment : fragmentManager.getFragments()) {
            fragmentManager.beginTransaction().remove(fragment).commit();
        }
        switch (data.getType()) {
            case "text": {
                FragmentText fragment = FragmentText.newInstance(data.getPayload());
                fragmentManager.beginTransaction().add(R.id.linearLayout,  fragment).commit();
                break;
            }
            case "webpage": {
                Fragment fragment = FragmentWeb.newInstance(data.getPayload());
                fragmentManager.beginTransaction().add(R.id.linearLayout,  fragment).commit();


                break;
            }
        }


    }

    public void loadPost() {

        postID = app.getCurrentPostID();
        app.getApiService().getApi().getPost(postID).enqueue(new Callback<Responce>() {
            @Override
            public void onResponse(Call<Responce> call, Response<Responce> response) {
                Responce post = response.body();
                if (post == null) {
                    loadingProgressBar.setVisibility(View.VISIBLE);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(() -> {
                        loadingProgressBar.setVisibility(View.GONE);
                        loadPost();
                    }, 50000);
                }else {
                    setView(post);
                    getSupportActionBar().setTitle(app.getActionBarString());
                }
            }

            @Override
            public void onFailure(Call<Responce> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


}