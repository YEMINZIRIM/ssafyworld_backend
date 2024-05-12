package com.yeminjilim.ssafyworld.chat.handler;

import com.yeminjilim.ssafyworld.chat.dto.ChatDto;
import com.yeminjilim.ssafyworld.chat.entity.Chat;
import com.yeminjilim.ssafyworld.chat.repository.ChatRepository;
import com.yeminjilim.ssafyworld.jwt.JWTProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONWriter;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.net.URI;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;


@RequiredArgsConstructor
@Component
@Slf4j
public class CustomWebSocketHandler implements WebSocketHandler {

    private final Map<String, Sinks.Many<String>> sinks;
    private final ChatRepository chatRepository;
    private final JWTProvider jwtProvider;


    @Override
    public Mono<Void> handle(WebSocketSession session) {
        String chatRoom = extractChatRoom(session.getHandshakeInfo().getUri());

        var output = session.receive()
                .map(e -> e.getPayloadAsText())
                .map(e -> {
                    try {
                        JSONObject json = new JSONObject(e);

                        String accessToken = json.getString("accessToken");
                        String content = json.getString("content");
                        String createdAt = json.getString("createdAt");

                        String realChatRoom = chatRoom.substring(4);
                        Map<String,Object> claims = jwtProvider.getClaims(accessToken);

                        return ChatDto.builder()
                                .groupInfoId(Long.valueOf(realChatRoom))
                                .senderId(Long.parseLong((String) claims.get("sub")))
                                .senderName((String) claims.get("name"))
                                .content(content)
                                .createdAt(createdAt)
                                .build();
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                        return null;
                    }
                })
                .filter(chatDto -> chatDto != null) // Filter out invalid messages
                .flatMap(chatDto -> chatRepository.save(chatDto.toEntity()));


        // 해당 채팅방의 Sink로 메시지 전송
        output.subscribe(s -> sinks.get(chatRoom).emitNext(jsonString(s), Sinks.EmitFailureHandler.FAIL_FAST));

        return session.send(sinks.get(chatRoom).asFlux().map(session::textMessage));
    }

    private String jsonString(Chat chat) {
        HashMap<String,String> map = new HashMap<>();
        map.put("sender",chat.getSenderName());
        map.put("content",chat.getContent());
        map.put("time",chat.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        return JSONObject.valueToString(map);
    }

    // URL에서 채팅방 식별
    private String extractChatRoom(URI uri) {
        String path = uri.getPath();
        return path.substring(path.lastIndexOf("/") + 1);
    }
}
