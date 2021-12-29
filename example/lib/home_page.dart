import 'package:flutter/material.dart';
import 'package:yin_pay/yin_pay.dart';
import 'package:yin_pay_example/alipay_native_page.dart';
import 'package:yin_pay_example/alipay_wap_page.dart';

///
class HomePage extends StatelessWidget {
  ///内容视图
  Widget contentView(BuildContext context) {
    List<Widget> children = [];

    children.add(ElevatedButton(
      child: Text('支付宝Native'),
      onPressed: () => toAlipayByNative(context),
    ));

    children.add(ElevatedButton(
      child: Text('支付宝Wap'),
      onPressed: () => toAlipayByWap(context),
    ));

    children.add(ElevatedButton(
      child: Text('微信注册'),
      onPressed: () => registerWeChatApp(context),
    ));

    children.add(ElevatedButton(
      child: Text('微信是否安装'),
      onPressed: () => isInstallWeChatApp(context),
    ));

    children.add(ElevatedButton(
      child: Text('获取微信Code'),
      onPressed: () => getWeChatCode(context),
    ));

    children.add(ElevatedButton(
      child: Text('微信支付'),
      onPressed: () => weChatPay(context),
    ));

    return Container(
      child: Column(children: children),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('支付宝Native'),
      ),
      body: contentView(context),
    );
  }

  ///跳转支付宝-原生
  void toAlipayByNative(BuildContext context) {
    Navigator.push(context, MaterialPageRoute(builder: (_) => AlipayNativePage()));
  }

  ///跳转支付宝-网页
  void toAlipayByWap(BuildContext context) {
    Navigator.push(context, MaterialPageRoute(builder: (_) => AlipayWapPage()));
  }

  ///判断微信是否安装
  void isInstallWeChatApp(BuildContext context) async {
    String isInstalled = await YinPay.invokeWeChatInstalled();
    print('test wechat isInstalled=$isInstalled');
  }

  ///注册微信
  void registerWeChatApp(BuildContext context) async {
    String appId = 'wx1234567890123456';
    String value = await YinPay.invokeWeChatRegister(appId);
    print('test value=$value');
  }

  ///获取微信Code
  void getWeChatCode(BuildContext context) async {
    // 获取code后,通过[https://api.weixin.qq.com/sns/oauth2/access_token?appid={appid}&secret={secret}&code={code}&grant_type=authorization_code]去获取openId
    // 接口最好由服务器去请求，防止secret泄漏
    ///apk 签名需要与微信开放平台设置一致
    String code = await YinPay.invokeGetWeChatCode();
    print('test code=$code');
  }

  ///微信支付
  void weChatPay(BuildContext context) async {
    ///apk 签名需要与微信开放平台设置一致
    String orderInfo =
        "{\"appid\":\"wx1234567890123456\",\"noncestr\":\"mb3x2f7pui60cmz1k71z1l7jjd2fvmj7\",\"package\":\"Sign=WXPay\",\"partnerid\":\"1234567890\",\"prepayid\":\"wx10164731896324b7d7593401647b2c0000\",\"timestamp\":\"1604998051\",\"sign\":\"849E5A6307FD1FE67E82B0A0F46AE4F9\"}";
    String result = await YinPay.invokeWeChatPay(orderInfo);
    print('test result=$result');
  }
}
