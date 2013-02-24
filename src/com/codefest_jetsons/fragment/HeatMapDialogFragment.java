package com.codefest_jetsons.fragment;

import android.app.DialogFragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.codefest_jetsons.R;

public class HeatMapDialogFragment extends DialogFragment {
	private WebView mWebView;

    public HeatMapDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.heat_map_layout, container);
        mWebView = (WebView) view.findViewById(R.id.webView);
        Uri path = 
        	Uri.parse("android.resource://com.codefest_jetsons/" + R.drawable.mapheat);
        mWebView.loadUrl(path.toString());
        return view;
    }

}
