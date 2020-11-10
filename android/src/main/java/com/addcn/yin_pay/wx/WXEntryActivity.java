package com.addcn.yin_pay.wx;

import android.app.Activity;
import android.util.Log;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import java.util.HashMap;

import io.flutter.plugin.common.MethodChannel;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private static String TAG = "WXEntryActivity";
    private static MethodChannel.Result result;

    public static void setResult(MethodChannel.Result rt) {
        result = rt;
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        int type = baseResp.getType();
        System.out.println("test WXEntryActivity onResp type = " + type);
        System.out.println("test WXEntryActivity onResp code = " + baseResp.errCode);
        System.out.println("test WXEntryActivity onResp value = " + baseResp.errStr);
        if (type == ConstantsAPI.COMMAND_SENDAUTH) {
            // 获取code后,通过[https://api.weixin.qq.com/sns/oauth2/access_token?appid={appid}&secret={secret}&code={code}&grant_type=authorization_code]去获取openId
            // 接口最好由服务器去请求，防止secret泄漏
            SendAuth.Resp authResp = (SendAuth.Resp) baseResp;
            final String code = authResp.code;
            System.out.println("test code=" + code);
            if (result != null) {
                result.success(code);
            } else {
                Log.e(TAG, "result 为 null");
            }
        } else if (type == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (result != null) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("openId", baseResp.openId);
                hashMap.put("errCode", "" + baseResp.errCode);
                hashMap.put("errStr", baseResp.errStr);
                hashMap.put("transaction", baseResp.transaction);
                result.success(hashMap.toString());
            } else {
                Log.e(TAG, "result 为 null");
            }
        }
        finish();
    }
}
