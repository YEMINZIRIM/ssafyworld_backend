package com.yeminjilim.ssafyworld.member.repository;

import com.yeminjilim.ssafyworld.member.entity.Question;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface QuestionRepository extends ReactiveCrudRepository<Question,Long> {
}
