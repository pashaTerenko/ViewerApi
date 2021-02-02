package com.terenko.viewerapi.Fragments;

import android.annotation.TargetApi;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.Fragment;

import com.terenko.viewerapi.API.Responce;
import com.terenko.viewerapi.R;

public class FragmentWeb extends Fragment {

    WebView webView;
    String content;

    public static FragmentWeb newInstance(Responce.Payload payload) {
        FragmentWeb fragmentWeb = new FragmentWeb();
        Bundle args = new Bundle();

        args.putString("Content", payload.getUrl());
        fragmentWeb.setArguments(args);
        return fragmentWeb;
    }

    public FragmentWeb() {
        super(R.layout.fragment_web);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_web,
                container, false);
        webView =  view.findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        WebViewClientImpl webViewClient = new WebViewClientImpl();
        webView.setWebViewClient(webViewClient);
        CookieManager.getInstance().setAcceptCookie(true);
        content = getArguments().getString("Content", "");
        webView.loadUrl(content);

        return view;
    }





    private class WebViewClientImpl extends WebViewClient {
        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }

        // Для старых устройств
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

}