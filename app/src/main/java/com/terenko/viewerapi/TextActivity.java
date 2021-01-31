package com.terenko.viewerapi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class TextActivity extends MainActivity {

    public static void start(Context caller, String text,String acurrentPost) {
        Intent intent = new Intent(caller, TextActivity.class);
        intent.putExtra(EXTRA_DATE_PAYLOAD, text);

        intent.putExtra(EXTRA_DATE_ID, acurrentPost);
        caller.startActivity(intent);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_view);


        TextView textView=findViewById(R.id.TextView);
        textView.setText(getIntent().getStringExtra(EXTRA_DATE_PAYLOAD));


    }

    @Override
    public void loadPost() {

        super.loadPost();

    }
}