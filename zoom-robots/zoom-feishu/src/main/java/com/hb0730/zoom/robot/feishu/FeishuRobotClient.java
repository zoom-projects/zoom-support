package com.hb0730.zoom.robot.feishu;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hb0730.zoom.robot.core.Assert;
import com.hb0730.zoom.robot.core.DefaultRobotMessageSend;
import com.hb0730.zoom.robot.core.JsonUtil;
import com.hb0730.zoom.robot.core.RobotException;
import com.hb0730.zoom.robot.core.RobotResponse;
import lombok.Getter;
import lombok.Setter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;

/**
 * 飞书机器人客户端
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2025/4/11
 * @see <a href="https://open.feishu.cn/document/client-docs/bot-v3/bot-overview">自定义机器人使用指南</a>
 */
@Getter
@Setter
public class FeishuRobotClient extends DefaultRobotMessageSend {
    private String webhook;
    private String secret;

    public FeishuRobotClient() {
    }

    public FeishuRobotClient(String webhook) {
        this.webhook = webhook;
    }

    public FeishuRobotClient(String webhook, String secret) {
        this.webhook = webhook;
        this.secret = secret;
    }

    @Override
    public RobotResponse doSend(String json) throws RobotException {
        Map<String, Object> body = JsonUtil.fromJson(json, new TypeReference<Map<String, Object>>() {
        });
        return doSend(body);
    }

    private RobotResponse doSend(Map<String, Object> body) throws RobotException {
        String signature = null;
        Integer timestamp = null;
        if (null != secret && !secret.isEmpty()) {
            // 生成时间戳
            timestamp = (int) (System.currentTimeMillis() / 1000);
            try {
                // 生成签名
                signature = GenSign(secret, timestamp);
            } catch (NoSuchAlgorithmException | InvalidKeyException e) {
                throw new RobotException("生成签名失败", e);
            }
            // 添加签名和时间戳到请求头
            body.put("timestamp", timestamp);
            body.put("sign", signature);
        }
        // 发送请求
        String json = JsonUtil.toJson(body);
        Assert.isNotBlank(webhook, "webhook 不能为空");
        Assert.isNotBlank(json, "消息不能为空");

        try {
            HttpClient httpClient = HttpClient.newBuilder().build();
            var request = java.net.http.HttpRequest.newBuilder()
                    .uri(java.net.URI.create(webhook))
                    .header(DEFAULT_CONTENT_TYPE_HEADER, DEFAULT_CONTENT_TYPE_JSON)
                    .POST(java.net.http.HttpRequest.BodyPublishers.ofString(json))
                    .build();
            var response = httpClient.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new RobotException("请求失败，状态码：" + response.statusCode());
            }
            String _body = response.body();
            if (_body == null) {
                throw new RobotException("请求失败，响应体为空");
            }
            // 解析响应
            RobotResponse robotResponse = JsonUtil.fromJson(_body, RobotResponse.class);
            if (robotResponse == null) {
                throw new RobotException("请求失败，响应体解析失败");
            }
            robotResponse.setSuccess(robotResponse.getCode() == 0);
            return robotResponse;
        } catch (Exception e) {
            throw new RobotException("请求失败", e);
        }
    }

    /**
     * 生成签名
     *
     * @param secret    密钥
     * @param timestamp 时间戳
     * @return 签名
     * @throws NoSuchAlgorithmException 算法异常
     * @throws InvalidKeyException      密钥异常
     */
    private static String GenSign(String secret, int timestamp) throws NoSuchAlgorithmException, InvalidKeyException {
        //把timestamp+"\n"+密钥当做签名字符串
        String stringToSign = timestamp + "\n" + secret;
        //使用HmacSHA256算法计算签名
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(stringToSign.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] signData = mac.doFinal(new byte[]{});
        return Base64.getEncoder().encodeToString(signData);
    }
}
