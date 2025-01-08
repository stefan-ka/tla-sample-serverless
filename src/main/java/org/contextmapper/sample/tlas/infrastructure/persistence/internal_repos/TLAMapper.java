// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package org.contextmapper.sample.tlas.infrastructure.persistence.internal_repos;

import org.contextmapper.sample.tlas.domain.tla.TLAStatus;
import org.contextmapper.sample.tlas.domain.tla.ThreeLetterAbbreviation;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;

public class TLAMapper {

	public static ThreeLetterAbbreviation tlaFromDynamoDB(Map<String, AttributeValue> items) {
		// TODO: alternative meanings, etc. not yet mapped
		return new ThreeLetterAbbreviation.TLABuilder(items.get("name").s())
				.withMeaning(items.get("meaning").s())
				.build();
	}

}
