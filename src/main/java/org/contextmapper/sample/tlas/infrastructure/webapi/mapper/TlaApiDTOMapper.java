package org.contextmapper.sample.tlas.infrastructure.webapi.mapper;

import org.contextmapper.sample.tlas.domain.tla.TLAGroup;
import org.contextmapper.sample.tlas.infrastructure.webapi.dtos.TLADto;
import org.contextmapper.sample.tlas.infrastructure.webapi.dtos.TLAGroupDto;

public class TlaApiDTOMapper {

    public static TLAGroupDto tlaGroupToDto(final TLAGroup group) {
        return new TLAGroupDto(group.getName().toString(),
                group.getDescription(),
                group.getTLAs().stream()
                        .map(tla -> new TLADto(tla.getName().toString(), tla.getMeaning())
                                .alternativeMeanings(tla.getAlternativeMeanings())
                                .link(tla.getLink()))
                        .toList()
        );
    }

}
