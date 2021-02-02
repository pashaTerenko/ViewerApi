package com.terenko.viewerapi.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.terenko.viewerapi.API.Responce;
import com.terenko.viewerapi.R;

public class FragmentText extends Fragment  {
    public FragmentText(){
        super(R.layout.fragment_text);
    }
TextView textView;
    String content;
    public static FragmentText newInstance(Responce.Payload payload) {
        FragmentText fragmentText = new FragmentText();
        Bundle args = new Bundle();

        args.putString("Content", payload.getText());
        fragmentText.setArguments(args);
        return fragmentText;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_text,
                container, false);
        textView=view.findViewById(R.id.TextView);
        content = getArguments().getString("Content", "");
        textView.setText(content);
        return view;
    }

}