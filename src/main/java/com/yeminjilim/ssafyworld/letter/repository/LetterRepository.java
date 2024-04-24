package com.yeminjilim.ssafyworld.letter.repository;

import com.yeminjilim.ssafyworld.letter.entity.Letter;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface LetterRepository extends ReactiveCrudRepository<Letter, Long> {

}
