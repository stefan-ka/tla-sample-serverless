package org.contextmapper.sample.tlas.infrastructure.migration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.contextmapper.sample.tlas.application.TlaApplicationService;
import org.contextmapper.sample.tlas.domain.tla.ThreeLetterAbbreviation;
import org.springframework.stereotype.Component;

import java.util.function.Function;

import static org.contextmapper.sample.tlas.domain.tla.TLAStatus.ACCEPTED;

@Component
public class DatabaseSeedHandler implements Function<String, String> {

    private static Log logger = LogFactory.getLog(DatabaseSeedHandler.class);

    private TlaApplicationService service;

    public DatabaseSeedHandler(final TlaApplicationService service) {
        this.service = service;
    }

    @Override
    public String apply(String unused) {
        logger.info("DatabaseSeedHandler called");
        try {
            service.save(new ThreeLetterAbbreviation.TLABuilder("TLA")
                    .withMeaning("Three Letter Abbreviation")
                    .withAlternativeMeaning("Three Letter Acronym")
                    .withStatus(ACCEPTED)
                    .build());

            service.save(new ThreeLetterAbbreviation.TLABuilder("ADR")
                    .withMeaning("Architectural Decision Record")
                    .withLink("https://adr.github.io/")
                    .withStatus(ACCEPTED)
                    .build());

            service.save(new ThreeLetterAbbreviation.TLABuilder("DDD")
                    .withMeaning("Domain-Driven Design")
                    .withAlternativeMeaning("Degenerative Disc Disease")
                    .withAlternativeMeaning("Dynamic Data Distribution")
                    .withLink("https://www.domainlanguage.com/ddd/reference/")
                    .withStatus(ACCEPTED)
                    .build());

            service.save(new ThreeLetterAbbreviation.TLABuilder("OHS")
                    .withMeaning("Open Host Service")
                    .withStatus(ACCEPTED)
                    .build());

            service.save(new ThreeLetterAbbreviation.TLABuilder("PL")
                    .withMeaning("Published Language")
                    .withStatus(ACCEPTED)
                    .build());

            service.save(new ThreeLetterAbbreviation.TLABuilder("CF")
                    .withMeaning("Conformist")
                    .withStatus(ACCEPTED)
                    .build());

            service.save(new ThreeLetterAbbreviation.TLABuilder("SK")
                    .withMeaning("Shared Kernel")
                    .withStatus(ACCEPTED)
                    .build());

            service.save(new ThreeLetterAbbreviation.TLABuilder("ACL")
                    .withMeaning("Anticorruption Layer")
                    .withStatus(ACCEPTED)
                    .build());

        } catch (Exception e) {
            logger.error("Internal error has happened", e);
            return e.getMessage();
        }
        return "ok";
    }
}
