package com.yeminjilim.ssafyworld.member.service;

import com.yeminjilim.ssafyworld.member.dto.GroupInfoDTO;
import com.yeminjilim.ssafyworld.member.entity.GroupInfo;
import com.yeminjilim.ssafyworld.member.repository.GroupInfoRepository;
import io.r2dbc.spi.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class GroupInfoServiceImpl implements GroupInfoService {

    private final GroupInfoRepository groupInfoRepository;
    private final ConnectionFactory connectionFactory;

    @Override
    public Mono<GroupInfo> findById(Long id) {
        return groupInfoRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("groupInfoId is not exist")))
                .log();
    }

    @Override
    public Flux<GroupInfo> findAll() {
        return groupInfoRepository.findAll()
                .log();
    }

    @Override
    public Mono<GroupInfo> save(GroupInfoDTO groupInfoDTO) {

        GroupInfo groupInfo = groupInfoDTO.toEntity();
        return groupInfoRepository
                .save(groupInfo)
                .onErrorMap((ex) -> {
                    throw new IllegalArgumentException("cannot save groupInfo" , ex);
                })
                .log();
    }

    @Override
    public Mono<GroupInfo> update(GroupInfoDTO groupInfoDTO) {
        GroupInfo groupInfo = groupInfoDTO.toEntity();
        return groupInfoRepository
                .save(groupInfo)
                .onErrorMap((ex) -> {
                    throw new IllegalArgumentException("cannot update groupInfo" , ex);
                })
                .log();
    }

    @Override
    public Mono<Long> delete(Long id) {

        return groupInfoRepository.existsById(id)
                .map((isExist) -> {
                    groupInfoRepository
                            .deleteById(id)
                            .onErrorMap((ex) -> {
                                throw new IllegalArgumentException("cannot delete groupInfo by id" , ex);
                            }).log();
                    return id;
                }).log();
    }

    @Override
    public Flux<Long> findAllOrdinal() {
        R2dbcEntityTemplate template = new R2dbcEntityTemplate(connectionFactory);
        String sql = "SELECT distinct g.ordinal 'ordinal' FROM group_info g";

        return template.getDatabaseClient()
                .sql(sql)
                .map(row -> row.get("ordinal", Long.class))
                .all()
                .log();
    }

    @Override
    public Flux<String> findAllRegion(Long ordinal) {
        R2dbcEntityTemplate template = new R2dbcEntityTemplate(connectionFactory);
        String sql = "SELECT distinct g.region 'region' FROM group_info g WHERE g.ordinal = :ordinal";

        return template.getDatabaseClient()
                .sql(sql)
                .bind("ordinal" , ordinal)
                .map(row -> row.get("region", String.class))
                .all()
                .log();
    }

    @Override
    public Flux<Long> findAllBan(Long ordinal, String region) {
        R2dbcEntityTemplate template = new R2dbcEntityTemplate(connectionFactory);
        String sql = "SELECT distinct g.ban 'ban' FROM group_info g WHERE g.ordinal = :ordinal and g.region = :region";

        return template.getDatabaseClient()
                .sql(sql)
                .bind("region",region)
                .bind("ordinal" , ordinal)
                .map(row -> row.get("ban", Long.class))
                .all()
                .log();
    }

    @Override
    public Mono<Long> findIdByOrdinalAndRegionAndBan(Long ordinal, String region, Long ban) {
        R2dbcEntityTemplate template = new R2dbcEntityTemplate(connectionFactory);

        String sql = "SELECT id from group_info where ordinal = :ordinal and region = :region and ban = :ban";

        return template.getDatabaseClient()
                .sql(sql)
                .bind("ordinal", ordinal)
                .bind("region", region)
                .bind("ban", ban)
                .map(row -> row.get("id", Long.class))
                .one();

    }




}
