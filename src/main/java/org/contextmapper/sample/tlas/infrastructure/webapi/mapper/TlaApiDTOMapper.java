package org.contextmapper.sample.tlas.infrastructure.webapi.mapper;

import org.contextmapper.sample.tlas.domain.tla.ThreeLetterAbbreviation;
import org.contextmapper.sample.tlas.infrastructure.webapi.dtos.TLADto;

public class TlaApiDTOMapper {

    public static TLADto tlaToDto(final ThreeLetterAbbreviation tla) {
        return new TLADto(tla.getName().toString(), tla.getMeaning())
                .alternativeMeanings(tla.getAlternativeMeanings())
                .link(tla.getLink());
    }

}
