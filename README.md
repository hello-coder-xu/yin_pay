# yin_pay

支付宝支付(AlipayHk与支付宝)

## Getting Started
- 支付宝
	- 先向服务器请求生成订单
	- 获取订单信息orderInfo
	- 向支付宝sdk发送orderInfo调起对应支付宝支付(根据订单信息内payment_inst值的不同调用支付宝)
	- 获取支付宝支付结果