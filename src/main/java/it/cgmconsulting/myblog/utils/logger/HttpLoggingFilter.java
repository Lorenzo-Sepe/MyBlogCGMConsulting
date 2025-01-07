package it.cgmconsulting.myblog.utils.logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
public class HttpLoggingFilter extends OncePerRequestFilter {

    private Long start, end;
    private String requestIdx;
    private final static String SPACE = "---------------------------------------------------------------------";

    private static final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        RepeatableContentCachingRequestWrapper requestWrapper = new RepeatableContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        logRequest(requestWrapper, request);
        filterChain.doFilter(requestWrapper, responseWrapper);
        logResponse(responseWrapper);
    }

    private void logRequest(RepeatableContentCachingRequestWrapper requestWrapper, HttpServletRequest request) throws IOException {
        String body = requestWrapper.readInputAndDuplicate();
        body = maskPassword(body);
        start = System.currentTimeMillis();
        requestIdx = request.getRequestId();
        Map<String, String[]> parameterMap = request.getParameterMap();
        StringBuffer parameters = new StringBuffer();
        if(!parameterMap.isEmpty()) {
            parameterMap.forEach((key, value) -> parameters.append("\n   ").append(key).append(": ").append(String.join(", ", value)));
        }
        log.info("\nREQUEST[{}] \n User: {}\n Uri: {}\n Body: {}\n Ip: {}\n @RequestParam: {}\n"+SPACE, requestIdx, SecurityContextHolder.getContext().getAuthentication().getName(), request.getQueryString()==null ? request.getRequestURI():request.getRequestURI()+"?"+request.getQueryString() , body, request.getRemoteAddr(), parameters);
    }

    private void logResponse(ContentCachingResponseWrapper responseWrapper) throws IOException {
        end = System.currentTimeMillis();
        long duration = end-start;
        log.info("\nRESPONSE[{}] \n Response: {} \n Duration: {}ms\n"+SPACE, requestIdx, new String(responseWrapper.getContentAsByteArray()), duration);
        responseWrapper.copyBodyToResponse();
    }

    public String maskPassword(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            if (jsonObject.has("password"))
                jsonObject.put("password", "*****");
            if (jsonObject.has("cardNumber"))
                jsonObject.put("cardNumber", "*****");
            return jsonObject.toString();
        } catch (Exception e) {
            return jsonString;
        }
    }

}
