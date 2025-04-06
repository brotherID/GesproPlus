package com.management.pro.config;

import io.micrometer.common.util.StringUtils;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class AuthorityInterceptor implements HandlerInterceptor {

    private final Tracer tracer;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        initMDC();
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        //No comment
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) {
        MDC.clear();
    }

    private void initMDC() {
        MDC.clear();
        String connectedUser = SecurityContextHandler.getConnectedUser();
        if (!StringUtils.isEmpty(connectedUser)) {
            MDC.put("user", connectedUser);
        }

        TraceContext traceContext = Optional.ofNullable(this.tracer.currentSpan())
                .map(Span::context)
                .orElseGet(() -> this.tracer.nextSpan().context());
        String traceId = traceContext.traceId();
        String spanId = traceContext.spanId();
        MDC.put("traceId", traceId);
        MDC.put("spanId", spanId);
    }
}
