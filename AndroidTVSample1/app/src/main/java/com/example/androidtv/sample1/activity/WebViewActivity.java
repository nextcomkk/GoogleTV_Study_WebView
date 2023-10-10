package com.example.androidtv.sample1.activity;

import android.content.SharedPreferences;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.os.Bundle;
import android.widget.Toast;

import com.example.androidtv.sample1.R;

/**
 * WebViewActivity initiates and assigns URL to WebView.
 */
public class WebViewActivity extends AppCompatActivity {

  private final String TAG = WebViewActivity.class.getSimpleName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.web_view);
    WebView webView = (WebView) findViewById(R.id.web_view);
    webView.getSettings().setJavaScriptEnabled(true);
    webView.setWebChromeClient(new WebChromeClient());

    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
    String url = sp.getString("webview_url", "");
    if (!url.equals("")) {
      webView.loadUrl(url);
    } else {
      Toast.makeText(this, "URLが設定されていません！",
              Toast.LENGTH_SHORT).show();
    }
  }
}

