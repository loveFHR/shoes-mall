package com.ewan.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 配置文件读取
 */
@Configuration
@Data
@Component
public class AlipayConfig {

    /**
     * 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
     */
    private String appId = "9021000136679634";

    /**
     * 商户私钥，您的PKCS8格式RSA2私钥
     */
    private String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCFRSDu4k2Yx1nrLxCQlrMVJ01aZL/C2sp57RQu4loTia6JSRs/3a9stBfyrN3+0sptyrGecpxuE/5fnbsTajuSvlh8GfG65b4HhrFkulvF0MDmdDEJ3iqXF3twRAOhljXQnT4eKQfUFURhuZarUgdv93urWL+FmX3WAyhqUaQMHugFFAVlc33HrW4iYt0sH4ocwtRX4/0mQbOH12cyflhXG9+FhgnRhK5mEJ6x9s4aMATGzZ+oqQhDLTAqM1dTyJ4aC+WvuO+cNvWWqncvcvqWoagsT78SIu2b2bcJ2nevpZDyyMIN+KGytVYY80Aqur18/muTnXABEONKEYzkvlgfAgMBAAECggEAEQOtW7I6s+mnmjYNCFilDmtRHdxE+yyRsxVIgV/Wni3ywYY622GDFv67G/lj8jeZLoE1QgRh4k/dJvntO2JRrr9sbpOlX/Ya170jgrekJdIr/oARDrB1gYq28LNOP1/9COA6YQFeBX6dl1hpWwADARF4kXyoHvdksGEA1OoNZpqQJ0BVuD4I+bA8MwSftvjKjhYec4nWCjgZZ/pUU+l59pLSv9Msui6maX2HbQ5x/s/Qpi5b7mgYrXtYsmjhc8rXdzpjZOebwmoALXZFzrPjukMpbEH9cVA0HEXuoYg6RwXeVIphiCxQJcenA0XqgYjFmrX57QJLaktvi/120g51GQKBgQDK8luwrBhcQ96ECVCy9cVLYsafau78ijfrRRBSahWL3/jnf1OH9Lg6xTu4/VAP9Nv5h6ySI/+GkMZAB5xAEnIlK5tN47IkYcfL4zC4Iseol+qAcg9AfMWnxsBDPnOlVLGaAI7MWCxjmME7biZGVsDloEGxqFftN/lH5SReLEN/gwKBgQCoG9p6N7IrxeutsWKcaRDb17wfFxwtvM18/cp30OP7y+UnfW8KZzxa5u4E/OSy6Lj/d5RGfvCUV84yBEBiiEglN7Eps8N2I5YIZeZixYLjqOVdiKR+HX+em8qEPb6joH5+iXFOdA0JModPS5Y+5qGZVsyOQiiVlZKMU+og77KmNQKBgCcSG5bauuHgaxFOXcvmRvJpdMWyJ4pU7ckMhcToCXfhSaD/wUwWT/V4jTPjCKvZvxO+C/oilgoJCzG+hVVMd88yIvcO0/fvHzDgbzuz7m5FuO67hrYqjz7qo48OkeMNeBem7dbnTrxp5yGS5il6gC50+NDEERFpSkOOGL9MlD4TAoGAQNU1JityuHUFVqBxsnbGvLJv5+GmyXDmIw1R13Gla0eQYfF7MJzt5vTm2N+bZ/Be4I+CjMmKCPvwFtQS7K04fboDxlyAxoAk7ao29uLLfvd1NeRr5AOGY2ob6XJSopJUFoL1Qtd465IkIw24rRuPOS8Dht22/zGHKj12rysiT/0CgYEAj1pctwwjimVMEgpxbmoc1wUQuEFbhOuREbQparn+xQmcypGPNznIH//vW+xXeEzzxPoV3gL/AVKhJ8Jj6iYiaYCFF8xpmzfjKJO8cm2V0kVzct1jsKg2RY7qJSQ4JeAMAGRmk2jmiXVotUaIk1OvnkbK0JMZlQd1EGfTqc6cxQM=";

    /**
     * 支付宝公钥,
     */
    private String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtVM03NJ3i919D21/0YZAwz7hYoL5UdJRT2FImIBB2KeGMuNAPDgD15uXjlJEajHLia+XwRTwHuqEnh0CHASASQhcb3xwB3MSopu1uvV6BU2WwdaG9aXjZ36OjVRI26ugIUxP5ZIkG5trMzBxagRMmWPIaTvHi0PASNc+5bvvHdWWcoU7/DUEowwctJtPRa9rMRJ0HREvvebl6IAJlS0vwV2mtmqBufNO6MNI2CB4VqijCBM3zXL/LwNH4BGA1+qQNxh4Z81ADElrnhBydqbQGcCCYnRJhrzXFQomvyj7PBxb5iJotz+du4iQ55aeB47pln3S+pQJM83rIx4SiqESvwIDAQAB";

    /**
     * 服务器异步通知页面路径需http://格式的完整路径，不能加?id=123这类自定义参数
     */
    private String notifyUrl = "http://139.9.181.192:2913/api/pay/notify";

    /**
     * 页面跳转同步通知页面路径 需http://格式的完整路径.
     * 支付完成后返回的地址
     */
    private String returnUrl = "http://47.109.108.209:9527/product/purchase/success";

    /**
     * 签名方式
     */
    private String signType = "RSA2";

    /**
     * 字符编码格式
     */
    private String charset = "utf-8";

    /**
     * 数据格式
     */
    public static String format = "json";

    /**
     * 支付宝网关
     */
    private String gatewayUrl = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";

    @Bean
    public AlipayClient registerAlipayConfig() {
        return (AlipayClient) new DefaultAlipayClient(getGatewayUrl(), appId, privateKey, format, charset, publicKey, signType);
    }

}

