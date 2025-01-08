package org.contextmapper.sample.tlas.infrastructure.webapi.dtos;

import java.util.List;

public class TLADto {

    private String name;
    private String meaning;
    private List<String> alternativeMeanings;

    public TLADto(String name, String meaning) {
        this.name = name;
        this.meaning = meaning;
    }

    public TLADto alternativeMeanings(List<String> alternativeMeanings) {
        this.alternativeMeanings = alternativeMeanings;
        return this;
    }

    public String getName() {
        return name;
    }

    public String getMeaning() {
        return meaning;
    }

    public List<String> getAlternativeMeanings() {
        return alternativeMeanings;
    }
}
