package org.contextmapper.sample.tlas.domain.tla;

import org.contextmapper.sample.tlas.domain.tla.exception.DuplicateTLANameException;
import org.contextmapper.sample.tlas.domain.tla.exception.TLANameAlreadyExistsInGroupException;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Entity;

import java.util.*;

@Entity
@AggregateRoot
public class TLAGroup {

    public static final String COMMON_GROUP = "common";

    private final ShortName name;
    private final String description;
    private final Set<ThreeLetterAbbreviation> tlas;

    private TLAGroup(final TLAGroupBuilder builder) {
        this.name = builder.name;
        this.description = builder.description;
        this.tlas = new TreeSet<>();

        for (ThreeLetterAbbreviation tla : builder.tlas) {
            if (tlas.contains(tla))
                throw new DuplicateTLANameException("Cannot create TLA group with duplicate TLA '" + tla.getName() + "'");
            tlas.add(tla);
        }
    }

    public ShortName getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void addTLA(final ThreeLetterAbbreviation tla) {
        if (tlas.contains(tla))
            throw new TLANameAlreadyExistsInGroupException("TLA '" + tla.getName() + "' already exists in group '" + this.name + "'.");
        tlas.add(tla);
    }

    public Set<ThreeLetterAbbreviation> getTLAs() {
        return Collections.unmodifiableSet(this.tlas);
    }

    public static class TLAGroupBuilder {
        private final ShortName name;
        private String description;
        private final List<ThreeLetterAbbreviation> tlas;

        public TLAGroupBuilder(final String name) {
            this.name = new ShortName(name);
            this.tlas = new ArrayList<>();
        }

        public TLAGroupBuilder withDescription(final String description) {
            this.description = description;
            return this;
        }

        public TLAGroupBuilder withTLA(final ThreeLetterAbbreviation tla) {
            this.tlas.add(tla);
            return this;
        }

        public TLAGroup build() {
            return new TLAGroup(this);
        }

    }

}
