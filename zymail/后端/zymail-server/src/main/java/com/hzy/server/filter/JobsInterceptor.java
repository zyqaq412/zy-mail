package com.hzy.server.filter;

import com.hzy.server.constant.SystemConstant;
import com.hzy.server.model.entity.JobVo;
import com.hzy.server.utils.HttpClientUtils;
import com.hzy.server.utils.IpUtils;
import com.hzy.server.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @title: JobsInterceptor
 * @Author zxwyhzy
 * @Date: 2023/10/27 15:35
 * @Version 1.0
 */
@Component
public class JobsInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisCache redisCache;
    @Value("${server.port}")
    private int port;
    @Autowired
    private HttpClientUtils httpClientUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 在请求到达Controller之前执行
        // 返回true表示继续处理，返回false表示停止请求处理
        // 获取请求路径
        String requestURI = request.getRequestURI();
        // 在日志中打印请求路径
        // System.out.println("请求路径: " + requestURI);
        // 请求路径: /jobs/pause/sendMail-df08d3b7-ad64-4044-9da2-2ed4337111d9/zymail-server

        // 获取HTTP请求方法
        String method = request.getMethod();
        System.out.println("拦截器_拦截路径：" + requestURI + "\t 请求方法：" + method);
        if (method.equals("GET")) return true;

        String[] split = requestURI.split("/");
        String thisIpaddr = IpUtils.getIpaddr() + ":" + port;

        String path = split[2];
        if (path.equals("modify")) {

            return true;
        }

        String jobName = split[split.length - 2];
        JobVo jobVo = redisCache.getCacheMapValue(SystemConstant.CACHE_JOBS, jobName);

        if (!jobVo.getIpaddr().equals(thisIpaddr)) {
            // 如果条件满足，将请求转发到jobVo.getIpaddr()
            System.out.println("转发请求");
            String forwardUrl = "http://" + jobVo.getIpaddr() + requestURI;
            if (method.equals("PUT")) {
                httpClientUtils.put(forwardUrl, "");
            } else if (method.equals("DELETE")) {
                httpClientUtils.delete(forwardUrl);
            }
            // 返回false表示请求已经被转发，不需要继续处理
            return false;
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 在Controller处理完请求后，渲染视图之前执行
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 在视图渲染完毕后执行
    }
}