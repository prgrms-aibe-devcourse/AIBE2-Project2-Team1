package com.example.campy.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class LoggerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String uri = request.getRequestURI();

        // 정적 리소스는 필터 제외
        if (isStaticResource(uri)) {
            filterChain.doFilter(request, response);
            return;
        }

        var req = new ContentCachingRequestWrapper(request);
        var res = new ContentCachingResponseWrapper(response);

        filterChain.doFilter(req, res);

        String method = req.getMethod();
        int status = res.getStatus();

        String reqBody = getReadableBody(req.getContentAsByteArray(), request.getContentType());
        String resBody = getReadableBody(res.getContentAsByteArray(), res.getContentType());

        log.info(
                "\n=================  [REQUEST] =================\n" +
                        "▶ Method : {}\n" +
                        "▶ URI    : {}\n" +
                        "▶ Body   : {}\n" +
                        "==============================================",
                method, uri, reqBody
        );

        log.info(
                "\n=================  [RESPONSE] ================\n" +
                        "◀ Status : {}\n" +
                        "◀ Body   : {}\n" +
                        "==============================================",
                status, resBody
        );

        res.copyBodyToResponse();
    }

    // 텍스트 기반 응답만 문자열로 디코딩
    private String getReadableBody(byte[] bodyBytes, String contentType) {
        if (contentType == null) return "(unknown content type)";

        if (contentType.contains("application/json") ||
                contentType.contains("text/plain") ||
                contentType.contains("application/xml") ||
                contentType.contains("application/x-www-form-urlencoded")) {
            return new String(bodyBytes, StandardCharsets.UTF_8).trim();
        }

        if (contentType.contains("text/html")) {
            return "(html content not logged)";
        }

        return "(binary or non-loggable content)";
    }



    // 확장자 기반 정적 리소스 필터링
    private boolean isStaticResource(String uri) {
        return uri.matches(".*\\.(css|js|png|jpg|jpeg|gif|ico|svg|woff|woff2|ttf|eot|map)$");
    }
}
