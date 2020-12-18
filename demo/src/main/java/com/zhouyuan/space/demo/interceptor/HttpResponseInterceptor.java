//package com.zhouyuan.space.demo.interceptor;
//
//import com.casic.htzy.log.constant.CommonConstant;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
///**
// * @auther: yuandong
// * @date: 2020/11/17
// * @description: TODO
// */
//public class HttpResponseInterceptor implements HandlerInterceptor {
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        String header = request.getHeader(CommonConstant.GLOBAL_TRACE_ID);
//        Object attribute = request.getAttribute(CommonConstant.GLOBAL_TRACE_ID);
//        if (StringUtils.isBlank(header) || null == attribute){
//            response.setHeader("setdsfjsdfajsdfa","null");
//            return true;
//        }
//        response.setHeader(CommonConstant.GLOBAL_TRACE_ID,header);
//        response.setHeader(CommonConstant.GLOBAL_TRACE_ID,attribute.toString());
//        return true;
//    }
//
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        String header = request.getHeader(CommonConstant.GLOBAL_TRACE_ID);
//        Object attribute = request.getAttribute(CommonConstant.GLOBAL_TRACE_ID);
//        response.setHeader(CommonConstant.GLOBAL_TRACE_ID,header);
//        response.setHeader(CommonConstant.GLOBAL_TRACE_ID,attribute.toString());
//    }
//}
