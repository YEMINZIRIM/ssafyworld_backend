package com.yeminjilim.ssafyworld.member.repository;

import com.yeminjilim.ssafyworld.member.entity.MemberInfo;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface MemberInfoRepository extends ReactiveCrudRepository<MemberInfo, Long> {

}
