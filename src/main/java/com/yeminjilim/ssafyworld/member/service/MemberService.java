package com.yeminjilim.ssafyworld.member.service;

import com.yeminjilim.ssafyworld.member.dto.GroupInfoDTO;
import com.yeminjilim.ssafyworld.member.dto.MemberDTO;
import com.yeminjilim.ssafyworld.member.dto.UpdateMemberDto;
import com.yeminjilim.ssafyworld.member.entity.Member;
import com.yeminjilim.ssafyworld.member.entity.MemberInfo;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface MemberService {

    Mono<Member> findById(Long id);
    Mono<Member> findBySub(String sub);

    Flux<Member> findAll();
    Mono<Member> findBySubAndProvider(String sub, String provider);
    Mono<MemberInfo> save(MemberDTO memberDTO);
    Mono<ResponseEntity<?>> update(UpdateMemberDto memberDTO, String sub);

    Mono<Void> delete(Long id);
    Mono<Boolean> existByOrdinalAndRegionAndBan(GroupInfoDTO groupInfoDTO, String name);

}
