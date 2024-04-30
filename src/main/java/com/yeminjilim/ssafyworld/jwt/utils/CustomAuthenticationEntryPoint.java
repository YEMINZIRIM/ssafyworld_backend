package com.yeminjilim.ssafyworld.jwt.utils;

import com.yeminjilim.ssafyworld.jwt.error.CustomJWTException;
import com.yeminjilim.ssafyworld.jwt.error.JWTErrorCode;
import net.minidev.json.JSONObject;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;
import org.springframework.security.web.server.authentication.ServerAuthenticationEntryPointFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;
import java.util.HashMap;

@Component
public class CustomAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

    private final ServerAuthenticationEntryPoint entryPointFailureHandler;

    public CustomAuthenticationEntryPoint() {
        this.entryPointFailureHandler = new HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED);
    }

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {

        if(ex instanceof CustomJWTException customJWTException) {
            customJWTException.printStackTrace();
            JWTErrorCode errorCode = customJWTException.getErrorCode();

            ServerHttpResponse serverHttpResponse = exchange.getResponse();
            serverHttpResponse.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            serverHttpResponse.setStatusCode(errorCode.getStatus());

            HashMap<String,String> map = new HashMap<>();
            map.put("message",customJWTException.getMessage());
            map.put("code",errorCode.getCode());
            String body = JSONObject.toJSONString(map);

            DataBufferFactory dataBufferFactory = serverHttpResponse.bufferFactory();
            DataBuffer buffer = dataBufferFactory.wrap(body.getBytes(Charset.defaultCharset()));

            return serverHttpResponse.writeWith(Mono.just(buffer))
                    .doOnError((error) -> DataBufferUtils.release(buffer));
        }

        return entryPointFailureHandler.commence(exchange,ex);
    }
}
