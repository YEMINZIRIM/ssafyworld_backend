package com.yeminjilim.ssafyworld.member.repository;

import com.yeminjilim.ssafyworld.member.entity.Member;
import reactor.core.publisher.Mono;

public interface MemberCustomRepository {

    Mono<Member> findByIdWithGroupInfo(Long id);
}
