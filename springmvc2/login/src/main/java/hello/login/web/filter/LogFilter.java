package hello.login.web.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;


@Slf4j
public class LogFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("log doFilter doFilter");

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        String uuid = UUID.randomUUID().toString();

        try {
            log.info("REQUEST [{}][{}]", uuid, requestURI);

            chain.doFilter(request, response);      // 있으면 다음 필터 호출 없으면 servlet 호출

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            log.info("RESPONSE [{}][{}]", uuid, requestURI);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("log doFilter init");
    }

    @Override
    public void destroy() {
        log.info("log doFilter destroy");
    }
}
