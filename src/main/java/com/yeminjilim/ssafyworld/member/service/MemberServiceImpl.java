package com.yeminjilim.ssafyworld.member.service;

import com.yeminjilim.ssafyworld.member.dto.MemberDTO;
import com.yeminjilim.ssafyworld.member.entity.GroupInfo;
import com.yeminjilim.ssafyworld.member.entity.Member;
import com.yeminjilim.ssafyworld.member.entity.MemberInfo;
import com.yeminjilim.ssafyworld.member.repository.MemberInfoRepository;
import io.r2dbc.spi.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private final ConnectionFactory connectionFactory;

    @Autowired
    private MemberInfoRepository memberInfoRepository;



    @Override
    public Mono<Member> findById(Long id) {

        R2dbcEntityTemplate template = new R2dbcEntityTemplate(connectionFactory);

        return template.getDatabaseClient()
                .sql("SELECT m.*, g.* FROM member m JOIN group_info g ON m.groupInfoId = g.id WHERE m.memberId = :id")
                .bind("id",id)
                .map(row -> {
                    MemberInfo memberInfo = MemberInfo.mapping(row);
                    GroupInfo groupInfo = GroupInfo.builder()
                            .id(row.get("groupInfoId",Long.class))
                            .ordinal(row.get("ordinal",Long.class))
                            .region(row.get("region",String.class))
                            .ban(row.get("ban", Long.class))
                            .build();

                    return new Member(memberInfo,groupInfo);
                })
                .first()
                .doOnNext(member -> log.info(member.toString()));
    }

    @Override
    public Flux<Member> findAll() {

        R2dbcEntityTemplate template = new R2dbcEntityTemplate(connectionFactory);

        return template.getDatabaseClient()
                .sql("SELECT m.*, g.* FROM member m JOIN group_info g ON m.groupInfoId = g.id")
                .map(row -> {
                    MemberInfo memberInfo = MemberInfo.mapping(row);
                    GroupInfo groupInfo = GroupInfo.builder()
                            .id(row.get("groupInfoId",Long.class))
                            .ordinal(row.get("ordinal",Long.class))
                            .region(row.get("region",String.class))
                            .ban(row.get("ban", Long.class))
                            .build();

                    return new Member(memberInfo,groupInfo);
                })
                .all()
                .doOnNext(member -> log.info(member.toString()));
    }

    @Override
    public Mono<MemberInfo> save(MemberDTO memberDTO) {

        R2dbcEntityTemplate template = new R2dbcEntityTemplate(connectionFactory);

        MemberInfo memberInfo = memberDTO.toEntity();
        return template.insert(MemberInfo.class)
                .using(memberInfo)
                .map(MemberInfo::getMemberId)
                .flatMap(memberId -> Mono.just(memberInfo));
    }



    @Override
    public Mono<MemberInfo> update(MemberDTO memberDTO) {
        return null;
    }

    @Override
    public Mono<Void> delete(Long id) {

        R2dbcEntityTemplate template = new R2dbcEntityTemplate(connectionFactory);

        return template.getDatabaseClient()
                .sql("DELETE FROM member where memberId = :id")
                .bind("id", id)
                .then()
                .doOnSuccess(result -> log.info("{}", id))
                .doOnError(error -> log.info("{}", id));
    }
}
