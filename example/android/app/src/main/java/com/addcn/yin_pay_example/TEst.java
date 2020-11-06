package com.addcn.yin_pay_example;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.addcn.yin_pay.AlipayUtil;
import com.addcn.yin_pay.YinPayPlugin;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import io.flutter.plugin.common.PluginRegistry.ActivityResultListener;

public class TEst implements FlutterPlugin, MethodCallHandler, ActivityAware {
    private Activity activity;
    private MethodChannel channel;
    private Result result;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        channel = new MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), "yin_pay");
        channel.setMethodCallHandler(this);
    }

    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "yin_pay");
        channel.setMethodCallHandler(new YinPayPlugin());
    }

    @Override
    public void onAttachedToActivity(@NonNull final ActivityPluginBinding binding) {
        this.activity = binding.getActivity();
        binding.addActivityResultListener(new ActivityResultListener() {
            @Override
            public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
                String resultValue = data.getStringExtra("result");
                System.out.println("test onActivityResult requestCode= " + requestCode + " resultCode= " + resultCode);
                if (result != null) {
                    result.success(result);
                }
                return false;
            }
        });
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {

    }

    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {

    }

    @Override
    public void onDetachedFromActivity() {

    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        this.result = result;
        String value = call.method;
        if (value.equals("alipay_native")) {
            String orderInfo = (String) call.argument("orderInfo");
            if (TextUtils.isEmpty(orderInfo)) {
                result.success("alipay error:orderInfo must be no null");
                return;
            }
            AlipayUtil alipayUtil = new AlipayUtil(activity, result);
            alipayUtil.invokeAlipayNative(orderInfo);
        } else if (value.equals("alipay_wep")) {
            String url = (String) call.argument("url");
            if (TextUtils.isEmpty(url)) {
                result.success("alipay error:url must be no null");
                return;
            }
            AlipayUtil alipayUtil = new AlipayUtil(activity, result);
            alipayUtil.invokeAlipayWep(url);
        } else {
            result.notImplemented();
        }
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }
}
