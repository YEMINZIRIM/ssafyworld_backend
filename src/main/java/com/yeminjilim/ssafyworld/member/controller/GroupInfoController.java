package com.yeminjilim.ssafyworld.member.controller;

import com.yeminjilim.ssafyworld.member.dto.GroupInfoDTO;
import com.yeminjilim.ssafyworld.member.service.GroupInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RequestMapping("/groupInfo")
@RestController
public class GroupInfoController {

    private final GroupInfoService groupInfoService;

    @GetMapping
    public Mono<ResponseEntity<?>> findAll(@RequestParam GroupInfoDTO request) {

        Long ordinal = request.getOrdinal();
        if(ordinal == null) {
            return groupInfoService.findAllOrdinal()
                    .collectList()
                    .map(ResponseEntity::ok);
        }

        String region = request.getRegion();
        if(region == null) {
            return groupInfoService.findAllRegion(ordinal)
                    .collectList()
                    .map(ResponseEntity::ok);
        }

        return groupInfoService.findAllBan(ordinal,region)
                .collectList()
                .map(ResponseEntity::ok);

    }
}
