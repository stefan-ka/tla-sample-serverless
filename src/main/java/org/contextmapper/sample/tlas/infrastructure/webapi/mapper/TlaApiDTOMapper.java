package org.contextmapper.sample.tlas.infrastructure.webapi.mapper;

import com.google.common.collect.Lists;
import org.contextmapper.sample.tlas.domain.tla.ThreeLetterAbbreviation;
import org.contextmapper.sample.tlas.infrastructure.webapi.dtos.TLADto;

public class TlaApiDTOMapper {

    public static TLADto tlaToDto(final ThreeLetterAbbreviation tla) {
        return new TLADto(tla.getName().toString(), tla.getMeaning())
                .alternativeMeanings(Lists.newArrayList(tla.getAlternativeMeanings()));
    }

}
