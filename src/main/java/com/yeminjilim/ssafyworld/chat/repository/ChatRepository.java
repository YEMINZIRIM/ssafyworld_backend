package com.yeminjilim.ssafyworld.chat.repository;


import com.yeminjilim.ssafyworld.chat.entity.Chat;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ChatRepository extends ReactiveCrudRepository<Chat, Long> {

    @Query("SELECT * FROM chat WHERE chat.groupInfoId = :groupInfoId and chat.createdAt < now() ORDER BY chat.createdAt DESC LIMIT 0, 200")
    Flux<Chat> findByGroupInfoIdOrderByCreatedAtDesc(@Param("groupInfoId") String groupInfoId);

}
