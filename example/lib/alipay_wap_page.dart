import 'package:flutter/material.dart';
import 'package:yin_pay/yin_pay.dart';

///支付宝Wap
class AlipayWapPage extends StatefulWidget {
  @override
  AlipayWapPageState createState() => AlipayWapPageState();
}

class AlipayWapPageState extends State<AlipayWapPage> {
  String url =
      'https://intlmapi.alipay.com/gateway.do?sign_type=MD5&_input_charset=utf-8&notify_url=http%3A%2F%2Fmerchants.wantu.cn%2Fpay%2Fnotify%2F%3Fstore_id%3D4781&return_url=http%3A%2F%2Fmerchants.wantu.cn%2Fpay%2Freturn&subject=203093884968&out_trade_no=100020201105150508044240&currency=HKD&total_fee=16190&secondary_merchant_id=3023&secondary_merchant_name=ADDCN+TECHNOLOGY+%28HK%29+CO.%2C+LIMITED+%E5%95%86%E5%9F%8E&secondary_merchant_industry=5311&product_code=NEW_WAP_OVERSEAS_SELLER&app_pay=Y&payment_inst=ALIPAYHK&partner=2088031755946862&service=create_forex_trade_wap&sign=be97caef8ec3405ce73bf2f03865b293';
  String result = 'no result';

  Widget contentView() {
    List<Widget> children = [];

    children.add(Container(
      child: Text('订单请求：$url'),
    ));

    children.add(Container(
      child: ElevatedButton(
        onPressed: invokeAlipayByWap,
        child: Text('调用支付宝Wap'),
      ),
    ));

    children.add(Container(
      child: Text('result:$result'),
    ));

    return Container(
      child: Column(children: children),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('支付宝Wap'),
      ),
      body: contentView(),
    );
  }

  void invokeAlipayByWap() async {
    String value = await YinPay.invokeAlipayByWap(url);
    result = value;
    setState(() {});
  }
}
