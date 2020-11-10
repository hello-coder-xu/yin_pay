import 'dart:async';

import 'package:flutter/services.dart';

class YinPay {
  static const MethodChannel _channel = const MethodChannel('yin_pay');

  static Future<String> invokeAlipayByNative(String orderInfo) async {
    final String result = await _channel.invokeMethod('ali_native_pay', {'orderInfo': orderInfo});
    return result;
  }

  static Future<String> invokeAlipayByWap(String url) async {
    final String result = await _channel.invokeMethod('ali_wap_pay', {'url': url});
    return result;
  }

  static Future<String> invokeWeChatRegister(String appId) async {
    final String result = await _channel.invokeMethod('wechat_register', {'appid': appId});
    return result;
  }

  static Future<bool> invokeWeChatInstalled() async {
    final bool result = await _channel.invokeMethod('wechat_install');
    return result;
  }

  static Future<String> invokeGetWeChatCode() async {
    final String result = await _channel.invokeMethod('wechat_get_code');
    return result;
  }

  static Future<String> invokeWeChatPay(String orderInfo) async {
    final String result = await _channel.invokeMethod('wechat_pay', {'orderInfo': orderInfo});
    return result;
  }
}
