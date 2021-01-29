package com.terenko.viewerapi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class TextActivity extends MainActivity {
    private static final String EXTRA_DATE_PAYLOAD = "TextViewActivity.EXTRA_DATE_PAYLOAD";
    private static final String EXTRA_DATE_ID = "TextViewActivity.EXTRA_DATE_ID";
    public static Intent start(Context caller, String text,String acurrentPost) {
        Intent intent = new Intent(caller, TextActivity.class);
        intent.putExtra(EXTRA_DATE_PAYLOAD, text);

        intent.putExtra(EXTRA_DATE_ID, acurrentPost);
        caller.startActivity(intent);
        return intent;
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