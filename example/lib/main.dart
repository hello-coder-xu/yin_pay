import 'package:flutter/material.dart';
import 'package:yin_pay/yin_pay.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  List<String> temp = [
    "_input_charset=\"utf-8\"&body=\"203062474003\"&currency=\"HKD\"&forex_biz=\"FP\"&notify_url=\"http://merchants.wantu.cn/pay/notify\"&out_trade_no=\"100020201102112034055143\"&partner=\"2088031755946862\"&payment_inst=\"ALIPAYHK\"&payment_type=\"1\"&product_code=\"NEW_WAP_OVERSEAS_SELLER\"&secondary_merchant_id=\"3023\"&secondary_merchant_industry=\"5311\"&secondary_merchant_name=\"ADDCN TECHNOLOGY (HK) CO., LIMITED 商城\"&seller_id=\"2088031755946862\"&service=\"mobile.securitypay.pay\"&subject=\"203062474003\"&total_fee=\"12\"&sign=\"CgPCwcB2Iyq9U6Osvk0A7jvBTmnvfWbEc7zCWUG4c9IE9GWxqSqpS%2BR1n07d24IToGZ09TtOW5bfty3xSzir75cLsvOcD7kwtBpDSNL4J9WH7yuJEbnMYBRUgmNg2%2FB2KirIJbk6ypEw%2BO74ID0%2FBDhzfFgsAm7j%2F8QS%2BURzfLE%3D\"&sign_type=\"RSA\"",
    "_input_charset=\"utf-8\"&body=\"203066194792\"&currency=\"HKD\"&forex_biz=\"FP\"&notify_url=\"http://merchants.wantu.cn/pay/notify\"&out_trade_no=\"100020201102112927074430\"&partner=\"2088031755946862\"&payment_inst=\"ALIPAYCN\"&payment_type=\"1\"&product_code=\"NEW_WAP_OVERSEAS_SELLER\"&secondary_merchant_id=\"3023\"&secondary_merchant_industry=\"5311\"&secondary_merchant_name=\"ADDCN TECHNOLOGY (HK) CO., LIMITED 商城\"&seller_id=\"2088031755946862\"&service=\"mobile.securitypay.pay\"&subject=\"203066194792\"&total_fee=\"11\"&sign=\"AbOiRtB8%2B4hFUBMrZzfz9jPfUzu3Ecqqb35dkrZPKPGpLklUUOShfloeh7lanKTZ4MEJFlIS0AZhmq8eA3ieI%2BjBdZmd7QMmI0Vb5tGTRIOxM5NOn0UmhULWvM3VTELC%2BJ9kYNIbYoqfUnTkRtO8FsiZ%2BQOqKP5Y9nNkAG9Wql4%3D\"&sign_type=\"RSA\""
  ];
  String orderInfo;
  bool isHk = true;
  String result = 'no result';

  @override
  void initState() {
    super.initState();
    orderInfo = isHk ? temp[0] : temp[1];
  }

  Widget contentView() {
    List<Widget> children = [];

    children.add(Container(
      child: Text('订单信息：$orderInfo'),
    ));

    children.add(Switch(
      value: isHk,
      onChanged: (value) {
        toggle();
      },
    ));

    String value = isHk ? 'Alipay Hk' : '支付宝';
    children.add(RaisedButton(
      onPressed: invokePay,
      child: Text('唤起$value'),
    ));

    children.add(Container(
      child: Text('result:$result'),
    ));

    return Column(children: children);
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Pay example app'),
        ),
        body: contentView(),
      ),
    );
  }

  void toggle() {
    isHk = !isHk;
    orderInfo = isHk ? temp[0] : temp[1];
    setState(() {});
  }

  void invokePay() async {
    String value = await YinPay.invokeAlipay(orderInfo);
    result = value;
    setState(() {});
  }
}
