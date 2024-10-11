package com.hb0730.zoom.desensitize.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.hb0730.zoom.base.utils.JsonUtil;
import com.hb0730.zoom.desensitize.core.annotation.Desensitize;
import com.hb0730.zoom.desensitize.core.enums.DesensitizationType;
import com.hb0730.zoom.desensitize.core.modifier.DesensitizeSerializerModifier;
import com.hb0730.zoom.desensitize.core.serializer.MapDesensitizeSerializer;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/10/10
 */
public class DesensitizeTest {

    @Test
    public void test() throws JsonProcessingException {
        TestModel model = new TestModel();
        model.setName("张三");
        model.setPhone("18888888888");
        model.setIdCard("123456789012345678");
        model.setAddress("北京市朝阳区");
        model.setEmail("qq@aa.com");
        model.setBankCard("1234567890123456789");
        model.setPassword("123456");
        model.setPassword2("123456");
        model.setPassword3("123456");

        String json = JsonUtil.DEFAULT.toJson(model);
        System.out.println(json);
        List<String> desensitizeFields = Arrays.asList("phone", "idCard", "address", "email", "bankCard", "password");
        List<String> ignoreFields = Arrays.asList("password2", "password3");
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        Map<String, Object> map = Map.of(
                "phone", "18888888888",
                "idCard", "123456789012345678",
                "address", "北京市朝阳区",
                "email", "11@qq.com",
                "bankCard", "1234567890123456789",
                "password", "123456",
                "password2", "123456",
                "password3", "123456",
                "test2", Map.of(
                        "phone", "18888888888",
                        "idCard", "123456789012345678",
                        "address", "北京市朝阳区",
                        "email", "sa@qq.com",
                        "bankCard", "1234567890123456789",
                        "password", "123456",
                        "password2", "123456",
                        "password3", "123456"
                )
        );


        module.addSerializer(new MapDesensitizeSerializer(desensitizeFields, ignoreFields));
        module.setSerializerModifier(new DesensitizeSerializerModifier(desensitizeFields, ignoreFields));
        objectMapper.registerModule(module);
        String json2 = objectMapper.writeValueAsString(map);
        System.out.println(json2);
        String json3 = objectMapper.writeValueAsString(model);
        System.out.println(json3);
    }

    @Data
    public static class TestModel implements Serializable {
        private String name;
        @Desensitize(type = DesensitizationType.MOBILE)
        private String phone;
        @Desensitize(type = DesensitizationType.ID_CARD)
        private String idCard;
        private String address;
        private String email;
        private String bankCard;
        private String password;
        private String password2;
        private String password3;
    }

    @Data
    public static class TestModel2 {
        private String test;
    }
}
