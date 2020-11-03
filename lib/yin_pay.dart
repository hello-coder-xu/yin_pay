import 'dart:async';

import 'package:flutter/services.dart';

class YinPay {
  static const MethodChannel _channel = const MethodChannel('yin_pay');

  static Future<String> invokeAlipay(String orderInfo) async {
    final String result = await _channel.invokeMethod('alipay', {'orderInfo': orderInfo});
    return result;
  }
}
