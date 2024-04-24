package com.yeminjilim.ssafyworld.member.controller;

import com.yeminjilim.ssafyworld.member.dto.MemberDTO;
import com.yeminjilim.ssafyworld.member.entity.Member;
import com.yeminjilim.ssafyworld.member.entity.MemberInfo;
import com.yeminjilim.ssafyworld.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    //회원가입
    @PostMapping("/join")
    public Mono<ResponseEntity<MemberDTO>> join(@RequestBody MemberDTO request) {

        return memberService.save(request)
                .map(MemberDTO::toDTO)
                .map(ResponseEntity::ok);
    }


}
