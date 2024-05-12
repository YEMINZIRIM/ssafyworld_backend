package com.yeminjilim.ssafyworld.chat.repository;


import com.yeminjilim.ssafyworld.chat.entity.Chat;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ChatRepository extends ReactiveCrudRepository<Chat, Long> {

    Flux<Chat> findByGroupInfoIdOrderByCreatedAtAsc(String groupInfoId, Pageable pageable);

}
