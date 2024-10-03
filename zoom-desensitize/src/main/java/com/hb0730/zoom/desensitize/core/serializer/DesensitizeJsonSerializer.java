package com.hb0730.zoom.desensitize.core.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.hb0730.zoom.base.utils.MaskUtil;
import com.hb0730.zoom.desensitize.core.annotation.FieldMask;

import java.io.IOException;
import java.util.Objects;

/**
 * jackson 脱敏序列化器
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/10/3
 */
public class DesensitizeJsonSerializer extends JsonSerializer<String> implements ContextualSerializer {
    private FieldMask fieldMask;


    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (!fieldMask.isDesensitization()) {
            gen.writeString(value);
            return;
        }
        switch (fieldMask.value()) {
            case CHINESE -> gen.writeString(MaskUtil.chineseName(value));
            case EMAIL -> gen.writeString(MaskUtil.email(value));
            case ID_CARD -> gen.writeString(MaskUtil.idCard(value));
            case FIX_PHONE -> gen.writeString(MaskUtil.fixedPhone(value));
            case Mobile -> gen.writeString(MaskUtil.mobile(value));
            case BANK_CARD -> gen.writeString(MaskUtil.bankCard(value));
            case ADDRESS -> gen.writeString(MaskUtil.address(value, 4));
            case API_KEY -> gen.writeString(MaskUtil.apiSecret(value));
            default -> gen.writeString(value);
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        FieldMask desensitize = property.getAnnotation(FieldMask.class);
        if (Objects.nonNull(desensitize)) {
            this.fieldMask = desensitize;
            return this;
        }
        return prov.findValueSerializer(property.getType(), property);
    }
}
