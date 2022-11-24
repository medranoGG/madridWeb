package com.dws.web.Security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class CSRFHandler implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new CSRFHandlerInterceptor());

    }

    class CSRFHandlerInterceptor implements HandlerInterceptor {
        @Override
        public void postHandle(final HttpServletRequest request,final @NonNull HttpServletResponse response, final @NonNull Object handler,final ModelAndView modelAndView) throws Exception{
            CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
            if(modelAndView != null){
                modelAndView.addObject("token", token.getToken());
                modelAndView.addObject("headerName",token.getHeaderName());
            }
        }

    }

}


