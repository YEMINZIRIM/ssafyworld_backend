package com.yeminjilim.ssafyworld.chat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import reactor.core.publisher.Sinks;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class WebsocketConfig {

    @Bean
    public SimpleUrlHandlerMapping handlerMapping(WebSocketHandler wsh) {
        Map<String, WebSocketHandler> urlMap = new HashMap<>();

        for(int i=1; i<=46; i++) {
            urlMap.put("/chat" + i, wsh);
        }

        return new SimpleUrlHandlerMapping(urlMap, 1);
    }

    @Bean
    public WebSocketHandlerAdapter webSocketHandlerAdapter() {
        return new WebSocketHandlerAdapter();
    }

    @Bean
    public Map<String, Sinks.Many<String>> sinks() {
        Map<String, Sinks.Many<String>> sinkMap = new HashMap<>();

        for(int i=1; i<=46; i++) {
            sinkMap.put("chat" + i, Sinks.many().multicast().directBestEffort());
        }
        return sinkMap;
    }

}

