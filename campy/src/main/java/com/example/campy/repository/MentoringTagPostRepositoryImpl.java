package com.example.campy.repository;

import com.example.campy.entity.QMentoringTag;
import com.example.campy.entity.QMentoringTagPost;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class MentoringTagPostRepositoryImpl implements MentoringTagPostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Map<Integer, List<String>> findTagNamesGroupedByOfferIds(List<Integer> offerIds) {

        QMentoringTagPost post = QMentoringTagPost.mentoringTagPost;
        QMentoringTag tag = QMentoringTag.mentoringTag;

        return queryFactory
                .select(Projections.tuple(post.mentoringOffer.offerId, tag.name))
                .from(post)
                .join(post.tag, tag)
                .where(post.mentoringOffer.offerId.in(offerIds))
                .fetch()
                .stream()
                .collect(Collectors.groupingBy(
                        tuple -> tuple.get(post.mentoringOffer.offerId),
                        Collectors.mapping(t -> t.get(tag.name), Collectors.toList())
                ));
    }
}
