package com.addcn.yin_pay;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.util.H5PayResultModel;
import com.alipay.sdk.app.H5PayCallback;

public class AlipayWapActivity extends Activity {

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = null;
        try {
            extras = getIntent().getExtras();
        } catch (Exception e) {
            finishAndCallBack(400, "extras must be not null");
            return;
        }
        if (extras == null) {
            finishAndCallBack(400, "extras must be not null");
            return;
        }
        String url = null;
        try {
            url = extras.getString("url");
        } catch (Exception e) {
            finishAndCallBack(400, "url must be not null");
            return;
        }
        if (TextUtils.isEmpty(url)) {
            finishAndCallBack(400, "url must be not null");
        }

        super.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LinearLayout layout = new LinearLayout(getApplicationContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layout.setOrientation(LinearLayout.VERTICAL);
        setContentView(layout, params);

        mWebView = new WebView(getApplicationContext());
        params.weight = 1;
        mWebView.setVisibility(View.VISIBLE);
        layout.addView(mWebView, params);

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);

        // 启用二方/三方 Cookie 存储和 DOM Storage
        // 注意：若要在实际 App 中使用，请先了解相关设置项细节。
        CookieManager.getInstance().setAcceptCookie(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(mWebView, true);
        }
        settings.setDomStorageEnabled(true);

        mWebView.setVerticalScrollbarOverlay(true);
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.loadUrl(url);

        // 启用 WebView 调试模式。
        // 注意：请勿在实际 App 中打开！
        WebView.setWebContentsDebuggingEnabled(true);
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            finishAndCallBack(400, "");
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(final WebView view, String url) {
            System.out.println("test shouldOverrideUrlLoading url=" + url);
            if (!(url.startsWith("http") || url.startsWith("https"))) {
                return true;
            }

            /**
             * 推荐采用的新的二合一接口(payInterceptorWithUrl),只需调用一次
             */
            final PayTask task = new PayTask(AlipayWapActivity.this);
            boolean isIntercepted = task.payInterceptorWithUrl(url, true, new H5PayCallback() {
                @Override
                public void onPayResult(final H5PayResultModel result) {
                    final String url = result.getReturnUrl();
                    System.out.println("test shouldOverrideUrlLoading getReturnUrl= " + url);
                    if (!TextUtils.isEmpty(url)) {
                        AlipayWapActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("test shouldOverrideUrlLoading H5PayCallback ");
                                view.loadUrl(url);
                            }
                        });
                        finishAndCallBack(200, url);
                    } else {
                        finishAndCallBack(400, "alipay no operation");
                    }
                }
            });

            System.out.println("test shouldOverrideUrlLoading isIntercepted =" + isIntercepted);
            /**
             * 判断是否成功拦截
             * 若成功拦截，则无需继续加载该URL；否则继续加载
             */
            if (!isIntercepted) {
                System.out.println("test shouldOverrideUrlLoading isIntercepted ");
                view.loadUrl(url);
            }
            return true;
        }
    }

    public void finishAndCallBack(int code, String result) {
        Intent intent = new Intent();
        intent.putExtra("result", result);
        setResult(code, intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.removeAllViews();
            try {
                mWebView.destroy();
            } catch (Throwable t) {
            }
            mWebView = null;
        }
    }
}
