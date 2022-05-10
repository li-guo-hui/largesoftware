package com.shanjupay.transaction.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 支付宝接口对接测试类
 * @author Administrator
 * @version 1.0
 **/

@Slf4j
@Controller
//@RestController//请求方法响应统一json格式
public class PayTestController {

    //应用id
    String APP_ID = "2021000117649526";
    //应用私钥
    String APP_PRIVATE_KEY = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDAE2sTjr6028I8AIGWO5gJ/OeSNDJOIzDaRI49rzxJOcoQZFRr4EQLqJ5LULu/G2Ooal+6BqABkZMpeEGjsD3KhNmt4M6moigDj2H+oIGYHmUg8iBNQiW0ObvkDyEw8/ag7iUlfB1BIhYJjebSwf2DDtrQjSF8wS9U4Bb5E5kA+d9zzFK3ZnHNnLgIwb0Nyr5G/dAOYCwSZUf4k3tSMo69VUKHqwMlnay7M3r0/pT9hwXkPSneTfplxjs4t8FvaUIP+iL3gzPZ9dnDcwuqeCib5PH+rFvKuwIVpVXH0aGy4JlpYOzqpr6pflz+bNi/LLNRjZIQ+Zw7rQcb0DlH7bY/AgMBAAECggEAalpWL74aWopUQmUFW+ojhWRD1PCR6jISGsla+UzOVL1q5SiolhLXmp0DCTDqxCamR9qepqKEdlnk4lF0Bu7PSBPHdD/GLP4cqdv6psK4/0HVPSjUOfMmbWSKZPz7o99x8Cns55SRnj8BdMjdxlUjyi2ve0qqACn3y0dJcSEo1yCN9E9Lz30Lx0qaRFgAMJx1fFQ+diZynCJsF+GDZx2j4oP+rkBSyO4f8srcmE/njXo5nPA8VkhldbU1jFgQnTFdXAtkl+d9IA8Ro7ZHXqYPdTDbNbh+lVZBNrAG+XTZc01MCNCv88lDWGU9e6ZBUbCVg4DWDFqGeYo8AHxJvao8AQKBgQDwvZJLSwg6ya6x/x8L994QXjm2W1yY087LpXYS+ZfBkwAWDJwPzuoHSPUyQGzVvuUtwUEkUt31klO2F1armBQViqqb/7wNObxBSVvOs+cNmXmADcoJXDFO+va2tpKpIP55Idc56YqIDqGglMMPzwa/gGg2jSgGLuiU/NTTcdhx2QKBgQDMQC3Wcbk5sZlbQ/nMkPtPXv16Pyt4lntWcF3HjTJtckqOGf483iUiuxWKYEbMlRTtMZa1IcDeAqqm+1t2iakw8O5zTe3U2e5L0LkFu40rEjhPWtjsS6AdkRQ5o4+n63VRydGJ4uhafMbo4Q7xLabN+Z+IkmAghL+NljtwQ25B1wKBgQDGBAgbKmJgiJfLDP/qhjz/1aE+37Mwebf1Reny/Z3XuSQu/rw3PIi+6UHVzaw3vEch9X2xdP/hCDUW5+eASRzsAx0GJ7n0XvL7+G0tfkikpQKNU8pFHLHqNv9LlqzXtK3b0PwJRJGQDAjh3rr7e9wfFG2jwUOUomzob3ZXXaMdMQKBgQCZQ3th09oygaLaygoyx4PcjiHUTnx3Myv/s7ebGseBOubY9IZC9EqXYh0Kxa26rA+U4MX+ywInVYbqX+jE7Q1pLREwsoRJWPKoL0n3FEIc1MIuQbROs3zFUu0DR0lvro5NPgye1AaYh9LWQrLspN3q52ofl/7Lx/DY9KdLWT3t5wKBgQCUbfyo5w1Nu5hx5niYPWkrXtg/A1GWffX0WrZbpwNMCuKXO5HHtdRNJZ58GgG+aKBdkyj5vGrEwFlUVPUDzUNzLApx8+XklHkQieF3JvJgidqAoQjqWrFUh6CglnYdpxWQqMnmEEMouYaDE1JO196VGlEVVzbAcBHy5fuIFHvNvQ==";
   //支付宝公钥
    String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmNfhBuaYiL6HEIXmmJ0cu6AGvcxgzEe/mjyqCrxGq+bQCe5SJbbgjchQyIo0sbLJqKba+KwutpM3DcbcnwCCyadhFmCx3UtgUKedpKnN+qo6QGVsE8skhT6KLHBK/vLe3yvcDWZ4Evxs+tZ4d3vDVyV6caOP+yr/l81QgnD9pn0bBuUVVA1vr+ZqZPPTPoxiQFuUypZAOyC17aeVJxsYTfO1s/4blp6ZFzXlfTM9+WCWISLDCmhW49D/Gih+UVzYeMaO3h5x/Z6DKdKmmUO404jZ/ovms0in8qdfGALsHkesJb45G0/CkhF/UYsw+54wSTNrcdQK9LLpNGzFujpgcwIDAQAB";
    String CHARSET = "utf-8";
    //支付宝接口的网关地址，正式"https://openapi.alipay.com/gateway.do"
    String serverUrl = "https://openapi.alipaydev.com/gateway.do";
    //签名算法类型
    String sign_type = "RSA2";

    @GetMapping("/alipaytest")
    public void alipaytest(HttpServletRequest httpRequest,
                       HttpServletResponse httpResponse) throws ServletException, IOException {
        //构造sdk的客户端对象
        AlipayClient alipayClient = new DefaultAlipayClient(serverUrl, APP_ID, APP_PRIVATE_KEY, "json", CHARSET, ALIPAY_PUBLIC_KEY, sign_type); //获得初始化的AlipayClient
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();//创建API对应的request
//        alipayRequest.setReturnUrl("http://domain.com/CallBack/return_url.jsp");
//        alipayRequest.setNotifyUrl("http://domain.com/CallBack/notify_url.jsp");//在公共参数中设置回跳和通知地址
        alipayRequest.setBizContent("{" +
                " \"out_trade_no\":\"20150420010101018\"," +  //订单号不能重复
                " \"total_amount\":\"88.88\"," +
                " \"subject\":\"Iphone6 16G\"," +
                " \"product_code\":\"QUICK_WAP_PAY\"" +
                " }");//填充业务参数
        String form="";
        try {
            //请求支付宝下单接口,发起http请求
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        httpResponse.setContentType("text/html;charset=" + CHARSET);
        httpResponse.getWriter().write(form);//直接将完整的表单html输出到页面
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
    }

}
