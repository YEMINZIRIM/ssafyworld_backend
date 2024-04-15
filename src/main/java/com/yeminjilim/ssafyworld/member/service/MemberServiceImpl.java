package com.yeminjilim.ssafyworld.member.service;

import com.yeminjilim.ssafyworld.member.dto.MemberDTO;
import com.yeminjilim.ssafyworld.member.entity.Member;
import com.yeminjilim.ssafyworld.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MemberServiceImpl implements MemberService {

    private MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public Mono<Member> findById(Long id) {
        return null;
    }

    @Override
    public Flux<Member> findAll() {
        return null;
    }

    @Override
    public Mono<Member> save(MemberDTO memberDTO) {
        return null;
    }

    @Override
    public Mono<Member> update(MemberDTO memberDTO) {
        return null;
    }

    @Override
    public Mono<Member> delete(Long id) {
        return null;
    }
}
