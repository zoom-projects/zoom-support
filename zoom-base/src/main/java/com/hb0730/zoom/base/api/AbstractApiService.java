package com.hb0730.zoom.base.api;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ArrayUtil;
import com.hb0730.zoom.base.Pair;
import com.hb0730.zoom.base.R;
import com.hb0730.zoom.base.pool.RegexPool;
import com.hb0730.zoom.base.utils.DateUtil;
import com.hb0730.zoom.base.utils.JsonUtil;
import com.hb0730.zoom.base.utils.MapUtil;
import com.hb0730.zoom.base.utils.RegexUtil;
import com.hb0730.zoom.base.utils.StrUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/10/24
 */
public abstract class AbstractApiService implements Api {

    @Override
    public R<?> execute(String token, Map<String, Object> params) {
        //参数要求
        Map<String, Pair<String, ParamCheck[]>> paramRule = getParamRule();
        //特殊要求
        Map<String, String[]> specialRule = getSpecialRule();

        //参数验证
        List<Pair<String, String>> invalidParams = checkParams(params, paramRule, specialRule);
        if (invalidParams != null && !invalidParams.isEmpty()) {
            return R.NG(JsonUtil.DEFAULT.toJson(
                    invalidParams.stream().map(Pair::getMessage).collect(Collectors.toList())
            ), invalidParams);
        }

        return doExecute(params);
    }

    @Override
    public boolean isSkipAuth() {
        return false;
    }

    /**
     * 执行业务
     *
     * @param params 参数
     * @return 结果
     */
    protected abstract R<?> doExecute(Map<String, Object> params);

    /**
     * 参数基本要求
     */
    protected abstract Map<String, Pair<String, ParamCheck[]>> getParamRule();

    /**
     * 参数特殊要求
     */
    protected abstract Map<String, String[]> getSpecialRule();

    /**
     * 配置KEY标志
     */
    protected final static String SPECIAL_KEY_FLAG = "`SPECIAL_KEY_FLAG`";

    /**
     * 获取特殊值
     *
     * @param key 参数名称
     * @return 特殊值
     */
    protected String[] getSpecialValues(String key) {
        return null;
    }

    public enum ParamCheck {
        /* 必须 */
        REQUIRED,
        /* 必须提供一个：需另外配置参数名称 */
        REQUIREDONE,
        /* 日期格式（yyyy-MM-dd） */
        DATE,
        /* 日期格式（yyyyMMdd） */
        DATE_yyyyMMdd,
        /* 日期时间格式（yyyy-MM-dd HH:mm:ss） */
        DATETIME,
        /* 日期时间格式 （yyyyMMddHHmmss）*/
        DATETIME_yyyyMMddHHmmss,
        /* 整数 */
        INTEGER,
        /* 数值 */
        NUMERIC,
        /* 特定值：需另外配置可选值 */
        SPECIAL,
        /* 正则表达式：需另外配置参数名称 */
        REGULAR,
        /* 文字字数限制：需另外配置限制长度 */
        LENGTH,
        /* 合法手机*/
        PHONE,
        /* 无要求 */
        NONE
    }

    /**
     * 接口参数验证
     *
     * @param paramsMap   接口参数
     * @param paramRule   参数要求
     * @param specialRule 特殊要求
     * @return 错误信息
     */
    protected List<Pair<String, String>> checkParams(Map<String, ?> paramsMap, Map<String, Pair<String, ParamCheck[]>> paramRule, Map<String, String[]> specialRule) {
        if (paramRule == null/* || specialRule == null*/) {
            return null;
        }
        if (specialRule == null) {
            specialRule = Collections.emptyMap();
        }

        //检查结果
        List<Pair<String, String>> subErrors = new ArrayList<>();

        //检测参数要求
        for (String param : paramRule.keySet()) {
            //参数值
            String value = MapUtil.get(paramsMap, param, String.class, null);

            //参数要求
            Pair<String, ParamCheck[]> infos = paramRule.get(param);
            for (ParamCheck check : infos.getMessage()) {
                switch (check) {
                    case REQUIRED:
                        if (StrUtil.isBlank(value)) {
                            subErrors.add(Pair.of("isv.missing-parameter", String.format("%s(%s)必须提供！", infos.getCode(),
                                    param)));
                        }
                        break;
                    case REQUIREDONE:
                        if (StrUtil.isBlank(value)) {
                            boolean hasValue = false;
                            String[] otherParams = specialRule.get(param);
                            for (String otherParam : otherParams) {
                                if (StrUtil.isNotBlank(MapUtil.get(paramsMap, otherParam, String.class, null))) {
                                    hasValue = true;
                                    break;
                                }
                            }

                            if (!hasValue) {
                                subErrors.add(new Pair<>("isv.missing-parameter", String.format("%s或（%s）必须提供一项！",
                                        param, ArrayUtil.join(otherParams, ","))));
                            }
                        }
                        break;
                    case DATE:
                        if (StrUtil.isNotBlank(value) && DateUtil.isValidateDate(value, "yyyy-MM-dd")) {
                            subErrors.add(Pair.of("isv.invalid-parameter", String.format("%s(%s)日期格式错误！", infos.getCode(),
                                    param)));
                        }
                        break;
                    case DATE_yyyyMMdd:
                        if (StrUtil.isNotBlank(value) && DateUtil.isValidateDate(value, "yyyyMMdd")) {
                            subErrors.add(Pair.of("isv.invalid-parameter", String.format("%s(%s)日期格式错误！", infos.getCode(),
                                    param)));
                        }
                        break;
                    case DATETIME:
                        if (StrUtil.isNotBlank(value) && DateUtil.isValidateDate(value, "yyyy-MM-dd HH:mm:ss")) {
                            subErrors.add(Pair.of("isv.invalid-parameter", String.format("%s(%s)日期时间格式错误！", infos.getCode(),
                                    param)));
                        }
                        break;
                    case DATETIME_yyyyMMddHHmmss:
                        if (StrUtil.isNotBlank(value) && DateUtil.isValidateDate(value, "yyyyMMddHHmmss")) {
                            subErrors.add(Pair.of("isv.invalid-parameter", String.format("%s(%s)日期时间格式错误！", infos.getCode(),
                                    param)));
                        }
                        break;
                    case INTEGER:
                        if (StrUtil.isNotBlank(value)) {
                            if (!"0".equals(value) && Convert.convert(Integer.class, value) == 0) {
                                subErrors.add(Pair.of("isv.invalid-parameter", String.format("%s(%s)整数格式错误！", infos.getCode(),
                                        param)));
                            }
                        }
                        break;
                    case NUMERIC:
                        if (StrUtil.isNotBlank(value) && !StrUtil.isNumeric(value)) {
                            subErrors.add(Pair.of("isv.invalid-parameter", String.format("%s(%s)数值格式错误！", infos.getCode(),
                                    param)));
                        }
                        break;
                    case SPECIAL:
                        if (cn.hutool.core.util.StrUtil.isBlank(value)) {
                            break;
                        }
                        String[] values = specialRule.get(param);
                        if (values != null && values.length > 0) {
                            if (SPECIAL_KEY_FLAG.equals(values[0])) {
                                values = getSpecialValues(values[1]);
                                if (values == null || values.length == 0) {
                                    break;
                                }
                            }
                            if (!Arrays.asList(values).contains(value)) {
                                subErrors.add(new Pair<>("isv.invalid-parameter", String.format("%s(%s)[%s]非法的参数值！", infos.getCode(), param, value)));
                            }
                        }
                        break;
                    case REGULAR:
                        String[] regular = specialRule.get(param + "_regular");
                        if (regular != null) {
                            if (StrUtil.isNotBlank(value) && !value.matches(regular[0])) {
                                subErrors.add(new Pair<>("isv.invalid-parameter", String.format("%s(%s)[%s]存在非法字符,只能使用字母/数字/中线/下划线！", infos.getCode(), param, value)));
                            }
                        }
                        break;
                    case LENGTH:
                        String[] length = specialRule.get(param + "_length");
                        if (length != null) {
                            if (StrUtil.isNotBlank(value) && value.length() > Convert.toInt(length[0], 0)) {
                                subErrors.add(new Pair<>("isv.invalid-parameter", String.format("%s(%s)长度超过限制！", infos.getCode(), param)));
                            }
                        }
                        break;
                    case PHONE:
                        if (StrUtil.isNotBlank(value) && !RegexUtil.isMatch(RegexPool.MOBILE, value)) {
                            subErrors.add(new Pair<>("isv.invalid-parameter", String.format("%s(%s)手机号码格式错误！", infos.getCode(), param)));
                        }
                        break;
                    case NONE:
                    default:
                        break;

                }
            }
        }

        return subErrors;
    }
}
