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

package org.contextmapper.sample.tlas.application;

import org.contextmapper.sample.tlas.application.exception.TLAGroupNameDoesNotExist;
import org.contextmapper.sample.tlas.application.exception.TLAGroupNameNotValid;
import org.contextmapper.sample.tlas.domain.tla.ShortName;
import org.contextmapper.sample.tlas.domain.tla.TLAGroup;
import org.contextmapper.sample.tlas.domain.tla.TLAGroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TlaGroupsApplicationService {

    private TLAGroupRepository repository;

    public TlaGroupsApplicationService(final TLAGroupRepository repository) {
        this.repository = repository;
    }

    public List<TLAGroup> findAllTLAGroups() {
        return repository.findAll();
    }

    public List<TLAGroup> findAllTLAsByName(final String name) {
        return findAllTLAGroups().stream()
                .filter(group -> group.getTLAs().stream().anyMatch(tla -> tla.getName().toString().equals(name)))
                .map(group -> new TLAGroup.TLAGroupBuilder(
                        group.getName().toString())
                        .withDescription(group.getDescription())
                        .withTLA(group.getTLAs().stream()
                                .filter(tla -> tla.getName().toString().equals(name))
                                .findFirst().orElseThrow())
                        .build())
                .toList();
    }

    public TLAGroup getGroupByName(final String name) {
        try {
            var shortName = new ShortName(name);
            var group = repository.findByName(shortName);
            if (group.isPresent()) {
                return group.get();
            } else {
                throw new TLAGroupNameDoesNotExist(name);
            }
        } catch (IllegalArgumentException e) {
            throw new TLAGroupNameNotValid(name);
        }
    }

    public TLAGroup save(final TLAGroup group) {
        return repository.save(group);
    }

}
