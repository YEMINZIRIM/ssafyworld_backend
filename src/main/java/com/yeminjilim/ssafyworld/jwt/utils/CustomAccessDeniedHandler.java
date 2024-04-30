package com.yeminjilim.ssafyworld.jwt.utils;

import com.yeminjilim.ssafyworld.jwt.error.CustomAccessDeniedException;
import net.minidev.json.JSONObject;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.HttpStatusServerAccessDeniedHandler;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;
import java.util.HashMap;

@Component
public class CustomAccessDeniedHandler implements ServerAccessDeniedHandler {

    private ServerAccessDeniedHandler handler;

    public CustomAccessDeniedHandler() {
        this.handler = new HttpStatusServerAccessDeniedHandler(HttpStatus.FORBIDDEN);
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException ex) {
        return Mono.defer(() -> Mono.just(exchange.getResponse()))
                .flatMap((response) -> {

            if(ex instanceof CustomAccessDeniedException customAccessDeniedException) {
                HttpStatus status = customAccessDeniedException.getErrorCode().getStatus();

                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                response.setStatusCode(status);

                String message = customAccessDeniedException.getMessage();
                String code = customAccessDeniedException.getErrorCode().getCode();

                HashMap<String,String> map = new HashMap<>();
                map.put("message",message);
                map.put("code",code);

                String body = JSONObject.toJSONString(map);

                DataBufferFactory dataBufferFactory = response.bufferFactory();
                DataBuffer buffer = dataBufferFactory.wrap(body.getBytes(Charset.defaultCharset()));

                return response.writeWith(Mono.just(buffer)).doOnError((error) -> DataBufferUtils.release(buffer));
            }

            return handler.handle(exchange,ex);
        });
    }

}
