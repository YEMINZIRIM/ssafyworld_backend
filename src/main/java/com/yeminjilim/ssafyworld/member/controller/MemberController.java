package com.yeminjilim.ssafyworld.member.controller;

import com.yeminjilim.ssafyworld.jwt.JWTProvider;
import com.yeminjilim.ssafyworld.member.dto.GroupInfoDTO;
import com.yeminjilim.ssafyworld.member.dto.LoginRequestDto;
import com.yeminjilim.ssafyworld.member.dto.MemberDTO;
import com.yeminjilim.ssafyworld.member.entity.Member;
import com.yeminjilim.ssafyworld.member.error.CustomMemberException;
import com.yeminjilim.ssafyworld.member.error.MemberErrorCode;
import com.yeminjilim.ssafyworld.member.service.MemberService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final JWTProvider jwtProvider;

    @Autowired
    public MemberController(JWTProvider jwtProvider, MemberService memberService) {
        this.jwtProvider = jwtProvider;
        this.memberService = memberService;
    }

    //회원가입
    @PostMapping("/register")
    public Mono<ResponseEntity<MemberDTO>> join(@RequestBody MemberDTO request) {

        return memberService.save(request)
                .map(MemberDTO::toDTO)
                .map(ResponseEntity::ok);
    }

    //회원수정
    @PutMapping("/member")
    public Mono<ResponseEntity<MemberDTO>> update(@RequestBody MemberDTO request) {

        return memberService.update(request)
                .map(MemberDTO::toDTO)
                .map(ResponseEntity::ok);
    }


    @PostMapping("/login")
    public Mono<ResponseEntity<String>> login(@RequestBody LoginRequestDto loginRequestDto) {
        return memberService.findBySubAndProvider(loginRequestDto.getSub(), loginRequestDto.getProvider())
                .flatMap(member -> {
                    String token = jwtProvider.makeToken(member);
                    return Mono.just(ResponseEntity.ok().body(token));
                })
                .switchIfEmpty(Mono.error(new CustomMemberException(MemberErrorCode.MEMBER_NOT_FOUND)));
    }

    @PostMapping("/logout")
    public Mono<ResponseEntity<?>> logout(@RequestHeader("token") String token) {
        // 토큰을 검증하고 유효한지 확인하는 코드

        return Mono.just(ResponseEntity.ok().body("Logged out successfully"));
    }

    //회원 정보 가져오기ㅣ
    @GetMapping("/member/info")
    public Mono<ResponseEntity<?>> getMemberInfo(ServerHttpRequest request) {
        try {
            Authentication authentication = jwtProvider.getToken(request);

            UserDetails userDetails = (UserDetails)authentication.getPrincipal();
            String sub = userDetails.getUsername();

            Mono<Member> findMember = memberService.findBySub(sub);
            return Mono.just(ResponseEntity.ok().body(findMember));

        } catch (Exception e) {
            return Mono.just(ResponseEntity.badRequest().body("Error retrieving member information: " + e.getMessage()));
        }
    }

    @GetMapping("/duplicate")
    public Mono<ResponseEntity<DuplicateMemberDTO>> isExistUserInGroup(
            GroupInfoDTO request,
            @RequestParam("name") String name
    ) {
        return memberService.existByOrdinalAndRegionAndBan(request,name)
                .map(DuplicateMemberDTO::new)
                .map(ResponseEntity::ok);
    }


    @Data
    private static class DuplicateMemberDTO {
        private Boolean duplicate;

        public DuplicateMemberDTO(Boolean duplicate) {
            this.duplicate = duplicate;
        }
    }
}
