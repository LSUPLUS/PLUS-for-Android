package com.example.cw.slidemeuetest.MainActivityFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cw.slidemeuetest.R;

/**
 * Created by cw on 2016/11/21.
 */

public class FragmentTwo extends Fragment {
    //讨论
//    private WebView webView;
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        webView=(WebView)getView().findViewById(R.id.id_webViewTwo);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.loadUrl("http://discuss.lsuplus.top/");
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tabtwo_layout,container,false);
    }
}
