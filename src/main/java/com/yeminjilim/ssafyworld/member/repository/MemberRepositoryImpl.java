package com.yeminjilim.ssafyworld.member.repository;

import com.yeminjilim.ssafyworld.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberCustomRepository {


    private final DatabaseClient databaseClient;

    @Override
    public Mono<Member> findByIdWithGroupInfo(Long id) {
        return null;
    }
}
