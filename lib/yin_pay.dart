import 'dart:async';

import 'package:flutter/services.dart';

class YinPay {
  static const MethodChannel _channel = const MethodChannel('yin_pay');

  static Future<String> invokeAlipayByNative(String orderInfo) async {
    final String result = await _channel.invokeMethod('alipay_native', {'orderInfo': orderInfo});
    return result;
  }

  static Future<String> invokeAlipayByWap(String url) async {
    final String result = await _channel.invokeMethod('alipay_wap', {'url': url});
    return result;
  }
}
