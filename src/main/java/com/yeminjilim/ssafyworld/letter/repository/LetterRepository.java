package com.yeminjilim.ssafyworld.letter.repository;

import com.yeminjilim.ssafyworld.letter.entity.Letter;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface LetterRepository extends ReactiveCrudRepository<Letter, Long> {
    Flux<Letter> findAllByToUser(Long toUser);
    Flux<Letter> findAllByToUserAndHiddenIsTrue(Long toUser);
    Flux<Letter> findAllByFromUser(Long fromUser);
}
