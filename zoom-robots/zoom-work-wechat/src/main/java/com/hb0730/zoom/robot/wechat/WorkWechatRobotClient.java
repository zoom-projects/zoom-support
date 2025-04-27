package com.hb0730.zoom.robot.wechat;

import com.hb0730.zoom.robot.core.Assert;
import com.hb0730.zoom.robot.core.DefaultRobotMessageSend;
import com.hb0730.zoom.robot.core.JsonUtil;
import com.hb0730.zoom.robot.core.RobotException;
import com.hb0730.zoom.robot.core.RobotResponse;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

/**
 * 企业微信机器人客户端
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2025/4/11
 * @see <a href="https://developer.work.weixin.qq.com/document/path/91770">群机器人</a>
 */
@Getter
@Setter
public class WorkWechatRobotClient extends DefaultRobotMessageSend {


    private String webhook;

    public WorkWechatRobotClient() {
    }

    public WorkWechatRobotClient(String webhook) {
        this.webhook = webhook;
    }

    @Override
    protected RobotResponse doSend(String json) throws RobotException {
        Assert.isNotBlank(webhook, "webhook 不能为空");
        HttpClient httpClient = HttpClient
                .newBuilder()
                .build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(webhook))
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
     * 响应
     */
    @Data
    public static class Response implements Serializable {
        /**
         * 错误码
         */
        private Integer errcode;
        /**
         * 错误信息
         */
        private String errmsg;

        public boolean isSuccess() {
            return errcode == 0;
        }

        public Integer getCode() {
            return errcode;
        }

        public String getMsg() {
            return errmsg;
        }

        /**
         * 响应
         *
         * @param json json
         * @return {@link RobotResponse}
         */
        public static RobotResponse of(String json) {
            Response res = JsonUtil.fromJson(json, Response.class);
            if (res == null) {
                return null;
            }
            RobotResponse response = new RobotResponse();
            response.setCode(res.getCode());
            response.setMsg(res.getMsg());
            response.setSuccess(res.isSuccess());
            return response;
        }

    }
}
