package com.css.zhm.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 描述:
 * 通用返回信息
 *
 * @author zhm
 * @create 2018-03-10 10:22
 */
public class Message {
    private static Map<String,Object> resultMap = new HashMap<String,Object>(16);

    /**
     * 成功
     * @return
     */
    public static Map<String, Object> success() {
        resultMap.put("code", 200);
        resultMap.put("message", "success");
        resultMap.put("result", "true");
        return resultMap;
    }

    /**
     * 失败
     * @return
     */
    public static Map<String, Object> failed(String message) {
        resultMap.put("code", 201);
        resultMap.put("message", message);
        resultMap.put("result", "false");
        return resultMap;
    }

    /**
     * 返回结果
     * @param
     * @return
     */
    public static Map<String, Object> returnResult(String result) {
        resultMap.put("code", 200);
        resultMap.put("message", "success");
        resultMap.put("result", result);
        return resultMap;
    }

    /**
     * 失败
     * @return
     */
    public static Map<String, Object> failed() {
        resultMap.put("code", 201);
        resultMap.put("message", "failed");
        resultMap.put("result", "false");
        return resultMap;
    }

    /**
     * 自定义返回
     * @return
     */
    public static Map<String, Object> custom(Integer code, String message, String result) {
        resultMap.put("code", code);
        resultMap.put("message", message);
        resultMap.put("result", result);
        return resultMap;
    }

}