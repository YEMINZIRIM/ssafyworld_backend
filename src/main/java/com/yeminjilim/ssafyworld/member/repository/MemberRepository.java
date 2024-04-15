package com.yeminjilim.ssafyworld.member.repository;

import com.yeminjilim.ssafyworld.member.entity.Member;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface MemberRepository extends ReactiveCrudRepository<Member, Long>,MemberCustomRepository {

//    @Query("SELECT m.* g.* FROM member m join group_info g ON m.groupInfoId = g.id WHERE m.id = :id")
//    public Mono<Member> findByIdWithGroupInfo(@Param("id") Long id);
}
