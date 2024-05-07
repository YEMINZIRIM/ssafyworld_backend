package com.yeminjilim.ssafyworld.member.service;

import com.yeminjilim.ssafyworld.member.dto.GroupInfoDTO;
import com.yeminjilim.ssafyworld.member.dto.MemberDTO;
import com.yeminjilim.ssafyworld.member.entity.Member;
import com.yeminjilim.ssafyworld.member.entity.MemberInfo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface MemberService {

    Mono<Member> findById(Long id);
    Mono<Member> findBySub(String sub);

    Flux<Member> findAll();
    Mono<Member> findBySubAndProvider(String sub, String provider);
    Mono<MemberInfo> save(MemberDTO memberDTO);
    Mono<MemberInfo> update(MemberDTO memberDTO);

    Mono<Void> delete(Long id);
    Mono<Boolean> existByOrdinalAndRegionAndBan(GroupInfoDTO groupInfoDTO, String name);
    Mono<Boolean> match(String sub, Long questionId, String answer);
    Mono<Void> updateName(Long id, String name);

}
