/*
 * Copyright 2023 The Context Mapper project team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.contextmapper.sample.tlas.infrastructure.persistence;

import org.contextmapper.sample.tlas.domain.tla.ShortName;
import org.contextmapper.sample.tlas.domain.tla.TLAGroup;
import org.contextmapper.sample.tlas.domain.tla.ThreeLetterAbbreviation;
import org.contextmapper.sample.tlas.domain.tla.TLAGroupRepository;
import org.contextmapper.sample.tlas.infrastructure.persistence.internal_repos.DynamoDBThreeLetterAbbreviationRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

@Repository
public class TLAGroupRepositoryImpl implements TLAGroupRepository {

    private DynamoDBThreeLetterAbbreviationRepository dynamoInternalRepo;

    public TLAGroupRepositoryImpl(final DynamoDBThreeLetterAbbreviationRepository dynamoInternalRepo) {
        this.dynamoInternalRepo = dynamoInternalRepo;
    }

    @Override
    public TLAGroup save(final TLAGroup group) {
        dynamoInternalRepo.putTLAGroup(group);
        return findByName(group.getName()).orElseThrow();
    }

    @Override
    public Optional<TLAGroup> findByName(final ShortName name) {
        var optionalTLAGroup = dynamoInternalRepo.findById(name.toString());
        if (optionalTLAGroup.isPresent())
            return of(optionalTLAGroup.get());
        return empty();
    }

    @Override
    public List<TLAGroup> findAll() {
        return dynamoInternalRepo.findAll();
    }
}
