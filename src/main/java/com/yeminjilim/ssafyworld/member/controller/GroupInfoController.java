package com.yeminjilim.ssafyworld.member.controller;

import com.yeminjilim.ssafyworld.member.dto.GroupInfoDTO;
import com.yeminjilim.ssafyworld.member.service.GroupInfoService;
import com.yeminjilim.ssafyworld.member.service.MemberService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Collections;

@RequiredArgsConstructor
@RequestMapping("/groupInfo")
@RestController
public class GroupInfoController {

    private final GroupInfoService groupInfoService;

    @GetMapping
    public Mono<ResponseEntity<?>> findAll(GroupInfoDTO request) {

        Long ordinal = request.getOrdinal();
        if(ordinal == null) {
            return groupInfoService.findAllOrdinal()
                    .collectList()
                    .doOnNext((list) -> list.sort(Long::compare))
                    .map(ResponseEntity::ok);
        }

        String region = request.getRegion();
        if(region == null) {
            return groupInfoService.findAllRegion(ordinal)
                    .collectList()
                    .doOnNext((list) -> list.sort(String::compareTo))
                    .map(ResponseEntity::ok);
        }
        Long ban = request.getBan();
        if(ban == null) {
            return groupInfoService.findAllBan(ordinal,region)
                    .collectList()
                    .doOnNext((list) -> list.sort(Long::compare))
                    .map(ResponseEntity::ok);
        }

        return groupInfoService.findIdByOrdinalAndRegionAndBan(ordinal, region, ban)
                .map(ResponseEntity::ok);

    }


}
