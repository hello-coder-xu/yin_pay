import 'package:flutter/material.dart';
import 'package:yin_pay_example/alipay_native_page.dart';
import 'package:yin_pay_example/alipay_wap_page.dart';

///
class HomePage extends StatelessWidget {
  ///内容视图
  Widget contentView(BuildContext context) {
    List<Widget> children = [];

    children.add(RaisedButton(
      child: Text('支付宝Native'),
      onPressed: () => toAlipayByNative(context),
    ));

    children.add(RaisedButton(
      child: Text('支付宝Wap'),
      onPressed: () => toAlipayByWap(context),
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
}
