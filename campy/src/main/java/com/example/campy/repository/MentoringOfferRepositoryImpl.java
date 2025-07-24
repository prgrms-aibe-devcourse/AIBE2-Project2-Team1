package com.example.campy.repository;

import com.example.campy.constant.MentoringStatus;
import com.example.campy.dto.mentoring.MentoringOfferSearchCondition;
import com.example.campy.dto.mentoring.response.MentoringOfferResponse;
import com.example.campy.entity.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.util.List;

@RequiredArgsConstructor
public class MentoringOfferRepositoryImpl implements MentoringOfferRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    private final QMentoringOffer offer = QMentoringOffer.mentoringOffer;
    private final QUser user = QUser.user;
    private final QMentoringTagPost tagPost = QMentoringTagPost.mentoringTagPost;
    private final QMentoringTag tag = QMentoringTag.mentoringTag;

    // 검색 로직 초안, 추후 삭제 예정
    @Override
    public Page<MentoringOfferRepository> search(MentoringOfferSearchCondition condition, Pageable pageable) {
/*
        QMentoringOffer offer = QMentoringOffer.mentoringOffer;
        QUser user = QUser.user;
        QMentoringTagPost tagPost = QMentoringTagPost.mentoringTagPost;
        QMentoringTag tag = QMentoringTag.mentoringTag;

        List<MentoringOffer> content = queryFactory
                .selectFrom(offer)
                .leftJoin(offer.user, user).fetchJoin()
                .leftJoin(tagPost).on(tagPost.mentoringOffer.eq(offer))
                .leftJoin(tagPost.tag, tag)  // 필드명이 'tag'임에 주의
                .where(
                        offer.isDeleted.eq(false),
                        keywordContains(condition.getKeyword()),
                        statusEq(condition.getStatus())
                )
                .distinct()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        // DTO 변환
        List<MentoringOfferResponse> responses = content.stream()
                .map(o -> {
                    List<String> tags = o.getMentoringTagPosts().stream()
                            .map(tp -> tp.getTag().getName())
                            .toList();
                    return MentoringOfferResponse.from(o, tags);
                })
                .toList();

        return new PageImpl<>(responses, pageable, total != null ? total : 0L);*/
        return null;
    }

    @Override
    public Page<MentoringOffer> searchWithKeyword(String keyword, Pageable pageable) {

        List<MentoringOffer> content = queryFactory
                .selectFrom(offer)
                .join(offer.user, user).fetchJoin()
                .where(
                        offer.isDeleted.eq(false),
                        keyword != null && !keyword.isBlank() ? (
                                offer.title.containsIgnoreCase(keyword)
                                        .or(offer.description.containsIgnoreCase(keyword))
                                        .or(user.nickname.containsIgnoreCase(keyword))
                        ) : null
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(offer.createdAt.desc())  // 정렬은 필요에 따라 수정
                .fetch();

        Long totalCount = queryFactory
                .select(offer.count())
                .from(offer)
                .join(offer.user, user)
                .where(
                        offer.isDeleted.eq(false),
                        keyword != null && !keyword.isBlank() ? (
                                offer.title.containsIgnoreCase(keyword)
                                        .or(offer.description.containsIgnoreCase(keyword))
                                        .or(user.nickname.containsIgnoreCase(keyword))
                        ) : null
                )
                .fetchOne();

        long total = totalCount != null ? totalCount : 0L;

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<MentoringOffer> searchByTag(String tagKeyword, Pageable pageable) {

        List<MentoringOffer> content = queryFactory
                .selectDistinct(offer)
                .from(offer)
                .join(tagPost).on(tagPost.mentoringOffer.eq(offer))
                .join(tagPost.tag, tag)
                .where(
                        offer.isDeleted.eq(false),
                        tag.name.containsIgnoreCase(tagKeyword)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(offer.createdAt.desc())
                .fetch();

        Long totalCount = queryFactory
                .select(offer.countDistinct())
                .from(offer)
                .join(tagPost).on(tagPost.mentoringOffer.eq(offer))
                .join(tagPost.tag, tag)
                .where(
                        offer.isDeleted.eq(false),
                        tag.name.containsIgnoreCase(tagKeyword)
                )
                .fetchOne();

        long total = totalCount != null ? totalCount : 0L;

        return new PageImpl<>(content,pageable, total);
    }

    private BooleanExpression keywordContains(String keyword){
        if (!StringUtils.hasText(keyword)) return null;

        return offer.description.containsIgnoreCase(keyword)
                .or(user.name.containsIgnoreCase(keyword))
                .or(tag.name.containsIgnoreCase(keyword));
    }

    private BooleanExpression statusEq(MentoringStatus status) {
        return status != null ? QMentoringOffer.mentoringOffer.status.eq(status) : null;
    }
}
