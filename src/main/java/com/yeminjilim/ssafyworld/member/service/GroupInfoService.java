package com.yeminjilim.ssafyworld.member.service;

import com.yeminjilim.ssafyworld.member.dto.GroupInfoDTO;
import com.yeminjilim.ssafyworld.member.entity.GroupInfo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GroupInfoService {

    Mono<GroupInfo> findById(Long id);
    Flux<GroupInfo> findAll();
    Mono<GroupInfo> save(GroupInfoDTO groupInfoDTO);
    Mono<GroupInfo> update(GroupInfoDTO groupInfoDTO);
    Mono<Long> delete(Long id);

    Flux<Long> findAllOrdinal();

    Flux<String> findAllRegion(Long ordinal);

    Flux<Long> findAllBan(Long ordinal, String region);

    Mono<Boolean> existByOrdinalAndRegionAndBan(GroupInfoDTO groupInfoDTO, String name);

}