package com.yeminjilim.ssafyworld.member.service;

import com.yeminjilim.ssafyworld.member.dto.MemberDTO;
import com.yeminjilim.ssafyworld.member.dto.UpdateMemberDto;
import com.yeminjilim.ssafyworld.member.entity.Member;
import com.yeminjilim.ssafyworld.member.entity.MemberInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
class MemberServiceImplTest {

    @Autowired
    private  MemberServiceImpl memberService;



    @Test
    @DisplayName("사용자 한명 조회 테스트")
    void findById () {
        Mono<Member> result = memberService.findById(10001L);

        StepVerifier.create(result)
                .assertNext(member -> {
                    assertEquals(10001L, member.getMemberInfo().getMemberId());
                    assertNotNull(member.getMemberInfo().getName());
                })
                .expectComplete()
                .verify();

    }

    @Test
    @DisplayName("사용자 전체 조회 테스트")
    void findAll() {
        Flux<Member> all = memberService.findAll();

        all.as(StepVerifier::create)
                .expectNextCount(4)
                .verifyComplete();

    }

    // @Test
    // @DisplayName("사용자 삭제 테스트")
    // void delete() {
    //     memberService.delete(10001L)
    //             .thenMany(memberService.findAll())
    //             .as(StepVerifier::create)
    //             .expectNextCount(3)
    //             .verifyComplete();
    // }


    @Test
    @DisplayName("사용자 추가 테스트")
    void save() {
        MemberInfo memberInfo = MemberInfo.builder()
                .name("LEEYELIM")
                .sub("10282887293503096962700")
                .provider("google")
                .groupInfoId(1L) // 적절한 그룹 정보 ID를 설정해주세요
                .serialNumber("LEEYELIMMMMM")
                .questionId(1L) // 적절한 질문 ID를 설정해주세요
                .answer("dsaddasasd")
                .build();

        MemberDTO memberDTO = MemberDTO.toDTO(memberInfo);

        Mono<MemberInfo> savedMember = memberService.save(memberDTO);

        StepVerifier.create(savedMember)
                .assertNext(saved -> {
                    assertNotNull(saved);
                    assertNotNull(saved.getMemberId());
                    assertEquals(memberInfo.getName(), saved.getName());
                    assertEquals(memberInfo.getSub(), saved.getSub());
                    assertEquals(memberInfo.getProvider(), saved.getProvider());
                    assertEquals(memberInfo.getGroupInfoId(), saved.getGroupInfoId());
                    assertEquals(memberInfo.getSerialNumber(), saved.getSerialNumber());
                    assertEquals(memberInfo.getQuestionId(), saved.getQuestionId());
                    assertEquals(memberInfo.getAnswer(), saved.getAnswer());
                })
                .expectComplete()
                .verify();
    }

//    @Test
//    @DisplayName("사용자 업데이트 테스트")
//    void update( ) {
//        MemberInfo memberInfo = MemberInfo.builder()
//                .memberId(10006L)
//                .name("LEEYERIM")
//                .sub("102828872935030969660")
//                .provider("google")
//                .groupInfoId(1L) // 적절한 그룹 정보 ID를 설정해주세요
//                .serialNumber("LEEYERIM")
//                .questionId(1L) // 적절한 질문 ID를 설정해주세요
//                .answer("질문바뀜요")
//                .build();
//
//        MemberDTO memberDTO = MemberDTO.toDTO(memberInfo);
//
//        Mono<MemberInfo> updateMember = memberService.update(new UpdateMemberDto());
//
//        StepVerifier.create(updateMember)
//                .assertNext(saved -> {
//                    assertNotNull(saved);
//                    assertEquals(memberInfo.getMemberId(), saved.getMemberId());
//                    assertEquals(memberInfo.getName(), saved.getName());
//                    assertEquals(memberInfo.getSub(), saved.getSub());
//                    assertEquals(memberInfo.getProvider(), saved.getProvider());
//                    assertEquals(memberInfo.getGroupInfoId(), saved.getGroupInfoId());
//                    assertEquals(memberInfo.getSerialNumber(), saved.getSerialNumber());
//                    assertEquals(memberInfo.getQuestionId(), saved.getQuestionId());
//                    assertEquals(memberInfo.getAnswer(), saved.getAnswer());
//                })
//                .expectComplete()
//                .verify();
//    }
}
