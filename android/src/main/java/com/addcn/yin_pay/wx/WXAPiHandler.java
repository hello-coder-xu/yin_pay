package com.addcn.yin_pay.wx;

import android.content.Context;
import android.text.TextUtils;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

public class WXAPiHandler {
    public static IWXAPI wxApi = null;
    private static Context context = null;

    public static void setContext(Context ctx) {
        context = ctx;
    }

    public static void register(MethodCall methodCall, MethodChannel.Result result) {
        String appId = methodCall.argument("appId");
        if (appId == null || appId.length() == 0) {
            result.error("-1", "无效appId ", appId);
            return;
        }

        if (wxApi == null) {
            IWXAPI api = WXAPIFactory.createWXAPI(context, appId);
            api.registerApp(appId);
            wxApi = api;
        }
        result.success("1");
    }


    public static void isWeChatInstalled(MethodChannel.Result result) {
        if (wxApi == null) {
            result.error("0", "请先初始化IWXApi", null);
        } else {
            result.success("1");
        }
    }


    public static void sendAuth() {
        final SendAuth.Req req = new SendAuth.Req();
//        req.scope = "snsapi_userinfo,snsapi_friend,snsapi_message,snsapi_contact";
        req.scope = "snsapi_userinfo";
        req.state = "none";
        wxApi.sendReq(req);
    }

    public static void pay(MethodCall methodCall, MethodChannel.Result result) {
        if (wxApi == null) {
            result.error("0", "请先初始化IWXApi", null);
            return;
        }
        String orderInfo = (String) methodCall.argument("orderInfo");
        if (TextUtils.isEmpty(orderInfo)) {
            result.error("-2", "无效orderInfo", null);
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(orderInfo);
            PayReq payReq = new PayReq();
            payReq.appId = jsonObject.getString("appid");
            payReq.partnerId = jsonObject.getString("partnerid");
            payReq.prepayId = jsonObject.getString("prepayid");
            payReq.packageValue = jsonObject.getString("package");
            payReq.nonceStr = jsonObject.getString("noncestr");
            payReq.timeStamp = jsonObject.getString("timestamp");
            payReq.sign = jsonObject.getString("sign");
            wxApi.sendReq(payReq);
        } catch (JSONException e) {
            e.printStackTrace();
            result.error("-2", "无效orderInfo", null);
        }
    }
}
