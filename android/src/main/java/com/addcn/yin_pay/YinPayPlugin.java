package com.addcn.yin_pay;


import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import io.flutter.plugin.common.PluginRegistry.ActivityResultListener;

import com.addcn.yin_pay.ali.AliApiHandler;
import com.addcn.yin_pay.wx.WXAPiHandler;
import com.addcn.yin_pay.wx.WXEntryActivity;

/**
 * YinPayPlugin
 */
public class YinPayPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware {
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
        WXAPiHandler.setContext(registrar.activity().getApplicationContext());
        AliApiHandler.setContext(registrar.activity());
    }

    @Override
    public void onAttachedToActivity(@NonNull final ActivityPluginBinding binding) {
        this.activity = binding.getActivity();
        WXAPiHandler.setContext(activity.getApplicationContext());
        AliApiHandler.setContext(activity);
        binding.addActivityResultListener(new ActivityResultListener() {
            @Override
            public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
                String resultValue = data.getStringExtra("result");
                System.out.println("test onActivityResult requestCode= " + requestCode + " resultCode= " + resultCode);
                if (result != null) {
                    result.success(resultValue);
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
        if (value.equals("ali_native_pay")) {
            AliApiHandler.invokeAlipayNative(call, result);
        } else if (value.equals("ali_wap_pay")) {
            AliApiHandler.invokeAlipayWep(call, result);
        } else if (value.equals("wechat_register")) {
            WXAPiHandler.register(call, result);
        } else if (value.equals("wechat_install")) {
            WXAPiHandler.isWeChatInstalled(result);
        } else if (value.equals("wechat_get_code")) {
            WXEntryActivity.setResult(result);
            WXAPiHandler.sendAuth();
        } else if (value.equals("wechat_pay")) {
            WXEntryActivity.setResult(result);
            WXAPiHandler.pay(call, result);
        } else {
            result.notImplemented();
        }
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }

}
