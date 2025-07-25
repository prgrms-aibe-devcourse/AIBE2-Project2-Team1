package com.example.campy.repository;

import java.util.List;
import java.util.Map;

public interface MentoringTagPostRepositoryCustom {
    Map<Integer, List<String>> findTagNamesGroupedByOfferIds(List<Integer> offerIds);
}
