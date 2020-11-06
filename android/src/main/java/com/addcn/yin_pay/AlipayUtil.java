package com.addcn.yin_pay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;

import com.alipay.sdk.app.PayTask;

import io.flutter.plugin.common.MethodChannel.Result;

import java.util.Map;

public class AlipayUtil {
    private Result result;
    private final Activity context;
    private static final int SDK_PAY_FLAG = 1;

    public AlipayUtil(Activity context, Result result) {
        this.result = result;
        this.context = context;
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    Map<String, String> resultValue = (Map<String, String>) msg.obj;
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
                    break;
                }
                default:
                    result.notImplemented();
                    break;
            }
        }
    };


    /**
     * 调用-原生支付宝
     *
     * @param orderInfoValue 订单信息
     */
    public void invokeAlipayNative(final String orderInfoValue) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(context);
                Map<String, String> result = alipay.payV2(orderInfoValue, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(runnable);
        payThread.start();
    }

    /**
     * 调用-网页支付宝
     *
     * @param url
     */
    public void invokeAlipayWep(String url) {
        WebView.setWebContentsDebuggingEnabled(true);
        Intent intent = new Intent(context, AlipayWapActivity.class);
        Bundle extras = new Bundle();
        extras.putString("url", url);
        intent.putExtras(extras);
        context.startActivityForResult(intent, 8591);
    }

}
