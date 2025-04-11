package com.hb0730.zoom.robot.dingtalk;

import com.hb0730.zoom.robot.core.Assert;
import com.hb0730.zoom.robot.core.DefaultRobotMessageSend;
import com.hb0730.zoom.robot.core.JsonUtil;
import com.hb0730.zoom.robot.core.RobotException;
import com.hb0730.zoom.robot.core.RobotResponse;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * 钉钉机器人客户端
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2025/4/11
 * @see <a href="https://open.dingtalk.com/document/orgapp/custom-bot-creation-and-installation">自定义机器人使用指南</a>
 */
@Getter
@Setter
public class DingtalkRobotClient extends DefaultRobotMessageSend {
    private String webhook;
    private String secret;

    public DingtalkRobotClient() {
    }

    public DingtalkRobotClient(String webhook) {
        this.webhook = webhook;
    }

    public DingtalkRobotClient(String webhook, String secret) {
        this.webhook = webhook;
        this.secret = secret;
    }

    @Override
    public RobotResponse doSend(String json) throws RobotException {
        Assert.isNotBlank(webhook, "webhook 不能为空");
        Assert.isNotBlank(json, "json 不能为空");
        String signature = null;
        String _webhook = webhook;
        // 生成签名
        if (null != secret && !secret.isEmpty()) {
            // 生成时间戳
            int timestamp = (int) (System.currentTimeMillis() / 1000);
            try {
                // 生成签名
                signature = genSign(secret, timestamp);
            } catch (NoSuchAlgorithmException | InvalidKeyException e) {
                throw new RobotException("生成签名失败", e);
            }
            // 拼接webhook
            _webhook = webhook + "&timestamp=" + timestamp + "&sign=" + signature;
        }
        // 发送请求
        HttpClient httpClient = HttpClient
                .newBuilder()
                .build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(java.net.URI.create(_webhook))
                .headers(DEFAULT_CONTENT_TYPE_HEADER, DEFAULT_CONTENT_TYPE_JSON)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        try {
            var response = httpClient.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new RobotException("请求失败，状态码：" + response.statusCode());
            }
            String body = response.body();
            if (body == null) {
                throw new RobotException("请求失败，响应体为空");
            }
            return Response.of(body);
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
     */
    private String genSign(String secret, int timestamp) throws NoSuchAlgorithmException, InvalidKeyException {
        String stringToSign = timestamp + "\n" + secret;
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
        String sign = new String(Base64.getEncoder().encode(signData), StandardCharsets.UTF_8);
        return URLEncoder.encode(sign, StandardCharsets.UTF_8);
    }

    @Data
    public static class Response {
        private Integer errcode;
        private String errmsg;

        public static RobotResponse of(String json) {
            Response response = JsonUtil.fromJson(json, Response.class);
            if (response == null) {
                return null;
            }
            RobotResponse robotResponse = new RobotResponse();
            robotResponse.setCode(response.getErrcode());
            robotResponse.setMsg(response.getErrmsg());
            robotResponse.setSuccess(response.getErrcode() == 0);
            return robotResponse;
        }

    }
}
