package com.example.webviewexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private String url = "https://m.naver.com"; // 모바일 환경이기 때문에 주소도 모바일로!



    @Override
    protected void onCreate(Bundle savedInstanceState) { // 앱이 실행되는 생명주기
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        webView = (WebView)findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);//자바script라는 부가적인 언어 허용할 것 이냐
        webView.loadUrl(url);//특정 url 주소 틀어라
        webView.setWebChromeClient(new WebChromeClient()); //webview환경을 구글 크롬에서 돌릴 수 있게 함(크롬 세팅)
        webView.setWebViewClient(new WebViewClinetClass()); //일반적인 webview 상황

    }

    //뒤로 가기 눌렀을 때 원래 화면으로 오게 하는 것
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) { //안드로이드의 다양한 키들을 입력했을 때 어떤 동작을 해줘라라고 지정할때 많이 사용
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()){   //만약에 뒤로 가기 버튼을 눌렀을 때 && 뒤로 갈 수 있게 되면
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class WebViewClinetClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) { //현재 페이지에 url을 읽어올 수 있음, 이걸 통해서 새 페이지에서 읽어올 수 있고 등등 다르게 사용 가능
            view.loadUrl(url);
            return true;
        }
    }
}