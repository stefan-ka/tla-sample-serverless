// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package org.contextmapper.sample.tlas.infrastructure.persistence.internal_repos;

import org.contextmapper.sample.tlas.domain.tla.TLAStatus;
import org.contextmapper.sample.tlas.domain.tla.ThreeLetterAbbreviation;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class TLAMapper {

    private static final String NAME = "name";
    private static final String MEANING = "meaning";
    private static final String ALTERNATIVE_MEANINGS = "alternative_meanings";
    private static final String URL = "url";
    private static final String STATUS = "status";

    public static ThreeLetterAbbreviation tlaFromDynamoDB(Map<String, AttributeValue> items) {
        var builder = new ThreeLetterAbbreviation.TLABuilder(items.get(NAME).s())
                .withMeaning(items.get("meaning").s())
                .withStatus(TLAStatus.valueOf(items.get(STATUS).s()));
        if (items.containsKey(ALTERNATIVE_MEANINGS)) {
            builder.withAlternativeMeanings(items.get(ALTERNATIVE_MEANINGS).l().stream()
                    .map(am -> am.s())
                    .collect(Collectors.toList()));
        }
        if (items.containsKey(URL)) {
            builder.withLink(items.get(URL).s());
        }
        return builder.build();
    }

    public static Map<String, AttributeValue> tlaToDynamoDb(ThreeLetterAbbreviation tla) {
        var map = new HashMap<String, AttributeValue>();
        map.put(NAME, AttributeValue.builder().s(tla.getName().toString()).build());
        map.put(MEANING, AttributeValue.builder().s(tla.getMeaning()).build());
        if (!tla.getAlternativeMeanings().isEmpty()) {
            map.put(ALTERNATIVE_MEANINGS, AttributeValue.builder().l(tla.getAlternativeMeanings().stream()
                    .map(am -> AttributeValue.fromS(am))
                    .collect(Collectors.toList())).build());
        }
        if (tla.getLink() != null && !"".equals(tla.getLink())) {
            map.put(URL, AttributeValue.builder().s(tla.getLink()).build());
        }
        map.put(STATUS, AttributeValue.builder().s(tla.getStatus().toString()).build());
        return map;
    }

}
