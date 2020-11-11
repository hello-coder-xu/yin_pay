package com.addcn.yin_pay.ali;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;

import com.alipay.sdk.app.PayTask;

import java.util.Map;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

public class AliApiHandler {
    private static String TAG = "AliApiHandler";
    private static Activity context;

    public static void setContext(Activity ctx) {
        context = ctx;
    }

    /**
     * 调用-原生支付宝
     */
    public static void invokeAlipayNative(MethodCall call, final MethodChannel.Result result) {
        final String orderInfo = (String) call.argument("orderInfo");
        if (TextUtils.isEmpty(orderInfo)) {
            result.error("-2", "无效orderInfo", orderInfo);
            return;
        }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(context);
                final Map<String, String> resultValue = alipay.payV2(orderInfo, true);
                Log.d(TAG, resultValue.toString());

                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        PayResult payResult = new PayResult(resultValue);
                        String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                        String resultStatus = payResult.getResultStatus();
                        // 判断resultStatus 为9000则代表支付成功
                        if (TextUtils.equals(resultStatus, "9000")) {
                            // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        } else {
                            // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        }
                        String resultString = payResult.toJson();
                        result.success(resultString);
                    }
                });
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(runnable);
        payThread.start();
    }

    /**
     * 调用-网页支付宝
     */
    public static void invokeAlipayWep(MethodCall call, final MethodChannel.Result result) {
        String url = (String) call.argument("url");
        if (TextUtils.isEmpty(url)) {
            result.error("-3", "无效url ", url);
            return;
        }
        WebView.setWebContentsDebuggingEnabled(true);
        Intent intent = new Intent(context, AlipayWapActivity.class);
        Bundle extras = new Bundle();
        extras.putString("url", url);
        intent.putExtras(extras);
        context.startActivityForResult(intent, 8591);
    }

}
