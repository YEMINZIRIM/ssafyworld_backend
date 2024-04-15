package com.yeminjilim.ssafyworld.member.service;

import com.yeminjilim.ssafyworld.member.dto.MemberDTO;
import com.yeminjilim.ssafyworld.member.entity.Member;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface MemberService {

    Mono<Member> findById(Long id);
    Flux<Member> findAll();

    Mono<Member> save(MemberDTO memberDTO);
    Mono<Member> update(MemberDTO memberDTO);

    Mono<Member> delete(Long id);
}
