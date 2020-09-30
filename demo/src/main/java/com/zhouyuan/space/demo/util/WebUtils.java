package com.zhouyuan.space.demo.util;

import com.casic.htzy.log.constant.RequestSourceEnum;

import javax.servlet.http.HttpServletRequest;

/**
 * Web相关工具类
 * @author yuandong
 * @since 2020/9/23
 */
public class WebUtils {


    /**
     * 获取请求ip
     *
     * @param request httpRequest
     * @return java.lang.String 127.0.0.1
     * @author yuanhao
     * @since 2020/9/18
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 如果是多级代理，那么取第一个ip为客户端ip
        if (ip != null && ip.contains(",")) {
            ip = ip.substring(0, ip.indexOf(",")).trim();
        }
        return ip;
    }

    /**
     * 判断http请求来源
     *
     * @param userAgent userAgent
     * @return java.lang.String
     * @author yuanhao
     * @since 2020/9/18
     */
    public static String judgeAgentType(String userAgent) {
        if (userAgent.contains("Windows NT")) {
            return RequestSourceEnum.WEB.getType();
        } else if (userAgent.contains("iPhone")) {
            return RequestSourceEnum.IOS.getType();
        } else if (userAgent.contains("Android")) {
            return RequestSourceEnum.ANDROID.getType();
        } else {
            return RequestSourceEnum.OTHER.getType();
        }
    }
}
