package com.cyh.yuhanbakery.controller;

import com.cyh.yuhanbakery.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Slf4j
@RestController
@RequestMapping("/api/payment/notify")
@RequiredArgsConstructor
public class WeChatPayNotifyController {

    private final OrderService orderService;

    @Value("${wechat.pay.api-v3-key}")
    private String apiV3Key;

    /**
     * 微信官方回调 Webhook (A 选项：本地模拟 HMAC-SHA256 安全签名校验)
     * 客户端需在 Headers 携带 Wechatpay-Timestamp, Wechatpay-Nonce, Wechatpay-Signature
     */
    @PostMapping("/wechat")
    public ResponseEntity<String> notifyWeChat(
            @RequestHeader("Wechatpay-Timestamp") String timestamp,
            @RequestHeader("Wechatpay-Nonce") String nonce,
            @RequestHeader("Wechatpay-Signature") String signature,
            @RequestBody String requestBody) {

        log.info("【微信回调】收到支付结果通知：Timestamp={}, Nonce={}, Signature={}", timestamp, nonce, signature);

        // 1. 签名解密校验 (A选项：使用配置的对称密钥进行 HMAC-SHA256 签名校验)
        boolean isSignatureValid = verifyMockSignature(timestamp, nonce, signature, requestBody);
        if (!isSignatureValid) {
            log.warn("【微信回调】签名校验失败！非法请求");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("FAIL_SIGNATURE");
        }

        // 2. 验签通过，解析 Body 并提取 OrderNo
        try {
            String orderNo = parseOrderNoFromMockBody(requestBody);
            log.info("【微信回调】验签通过，订单 {} 微信支付成功", orderNo);
            
            // 触发支付成功逻辑 (payMethod=2: 微信支付)
            orderService.paySuccess(orderNo, 2);
            
            return ResponseEntity.ok("SUCCESS");
        } catch (Exception e) {
            log.error("【微信回调】解析或状态流转出现异常：", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("FAIL_SERVER");
        }
    }

    private boolean verifyMockSignature(String timestamp, String nonce, String signature, String body) {
        try {
            // 构造加签文本串
            String message = timestamp + "\n" + nonce + "\n" + body + "\n";
            
            // 计算 HMAC-SHA256 签名
            Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(apiV3Key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            hmacSHA256.init(secretKeySpec);
            
            byte[] hashBytes = hmacSHA256.doFinal(message.getBytes(StandardCharsets.UTF_8));
            String expectedSignature = Base64.getEncoder().encodeToString(hashBytes);
            
            return expectedSignature.equals(signature);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            log.error("【微信回调】计算 HMAC-SHA256 模拟签名发生错误：", e);
            return false;
        }
    }

    private String parseOrderNoFromMockBody(String body) {
        // 支持简单字段过滤提取：{"orderNo": "ORDxxx"}
        if (body.contains("orderNo")) {
            int start = body.indexOf("orderNo") + 10;
            int end = body.indexOf("\"", start);
            if (start > 9 && end > start) {
                return body.substring(start, end);
            }
        }
        throw new IllegalArgumentException("请求体中未找到合法的 orderNo 字段");
    }
}
