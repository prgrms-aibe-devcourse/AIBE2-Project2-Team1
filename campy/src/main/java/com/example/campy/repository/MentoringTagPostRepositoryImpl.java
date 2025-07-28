package com.example.campy.repository;

import com.example.campy.entity.QMentoringTag;
import com.example.campy.entity.QMentoringTagPost;
import com.querydsl.core.Tuple;
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
        QMentoringTagPost tagPost = QMentoringTagPost.mentoringTagPost;
        QMentoringTag tag = QMentoringTag.mentoringTag;

        List<Tuple> result = queryFactory
                .select(tagPost.mentoringOffer.offerId, tag.name)
                .from(tagPost)
                .join(tagPost.tag, tag)
                .where(tagPost.mentoringOffer.offerId.in(offerIds))
                .fetch();

        return result.stream()
                .collect(Collectors.groupingBy(
                        tuple -> tuple.get(tagPost.mentoringOffer.offerId),
                        Collectors.mapping(tuple -> tuple.get(tag.name), Collectors.toList())
                ));
    }
}
