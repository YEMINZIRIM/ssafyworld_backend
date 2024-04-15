package com.yeminjilim.ssafyworld.member.service;

import com.yeminjilim.ssafyworld.member.dto.MemberDTO;
import com.yeminjilim.ssafyworld.member.entity.Member;
import com.yeminjilim.ssafyworld.member.entity.MemberInfo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface MemberService {

    Mono<Member> findById(Long id);
    Flux<Member> findAll();

    Mono<MemberInfo> save(MemberDTO memberDTO);
    Mono<MemberInfo> update(MemberDTO memberDTO);

    Mono<MemberInfo> delete(Long id);
}
