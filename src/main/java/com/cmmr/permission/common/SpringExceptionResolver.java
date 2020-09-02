package com.cmmr.permission.common;

import com.cmmr.permission.exception.ParamException;
import com.cmmr.permission.exception.PermissionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class SpringExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        String url = request.getRequestURL().toString();
        ModelAndView mv;
        String defaultMessage = "System Error";
        String illegalUrl = "illegal request suffix";

        //.json .html
        //这里我们要求项目的页面请求都以.html结尾，数据请求都以.json结尾
        if(url.endsWith(".json")){
            if(ex instanceof PermissionException || ex instanceof ParamException){
                JsonData result = JsonData.fail(ex.getMessage());
                mv = new ModelAndView("jsonView", result.toMap());
            }else{
                log.error("unknown json exception, url:" + url, ex);
                JsonData result = JsonData.fail(defaultMessage);
                mv = new ModelAndView("jsonView", result.toMap());
            }
        }else if(url.endsWith(".html")){
            log.error("unknown html exception, url:" + url, ex);
            JsonData result = JsonData.fail(defaultMessage);
            mv = new ModelAndView("exception/exception", result.toMap());
        }else{
            log.error("unknown exception, url:" + url, ex);
            JsonData result = JsonData.fail(defaultMessage + ":" + illegalUrl);
            mv = new ModelAndView("jsonView", result.toMap());
        }
        return mv;
    }
}
