package com.yeminjilim.ssafyworld.chat.handler;

import com.yeminjilim.ssafyworld.chat.dto.ChatDto;
import com.yeminjilim.ssafyworld.chat.repository.ChatRepository;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.net.URI;
import java.util.Map;


@Component
@Slf4j
public class CustomWebSocketHandler implements WebSocketHandler {

    private final Map<String, Sinks.Many<String>> sinks;
    private final ChatRepository chatRepository;


    public CustomWebSocketHandler(Map<String, Sinks.Many<String>> sinks, ChatRepository repository) {
        this.sinks = sinks;
        this.chatRepository = repository;
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        String chatRoom = extractChatRoom(session.getHandshakeInfo().getUri());

        var output = session.receive()
                .map(e -> e.getPayloadAsText())
                .map(e -> {
                    try {
                        JSONObject json = new JSONObject(e);
                        long senderId = json.getLong("memberId");
                        String sender = json.getString("sender");
                        String content = json.getString("content");
                        String realChatRoom = chatRoom.substring(4);


                        ChatDto chatDto = new ChatDto();
                        chatDto.setGroupInfoId(Long.valueOf(realChatRoom));
                        chatDto.setSenderId(senderId);
                        chatDto.setSenderName(sender);
                        chatDto.setContent(content);

                        return chatDto;
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                        return null;
                    }
                })
                .filter(chatDto -> chatDto != null) // Filter out invalid messages
                .flatMap(chatDto -> chatRepository.save(chatDto.toEntity()))
                .map(savedMessage -> savedMessage.getSenderName() + ": " + savedMessage.getContent());


        // 해당 채팅방의 Sink로 메시지 전송
        output.subscribe(s -> sinks.get(chatRoom).emitNext(s, Sinks.EmitFailureHandler.FAIL_FAST));

        return session.send(sinks.get(chatRoom).asFlux().map(session::textMessage));
    }

    // URL에서 채팅방 식별
    private String extractChatRoom(URI uri) {
        String path = uri.getPath();
        return path.substring(path.lastIndexOf("/") + 1);
    }
}
