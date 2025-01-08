package org.contextmapper.sample.tlas.infrastructure.webapi;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.contextmapper.sample.tlas.application.TlaApplicationService;
import org.contextmapper.sample.tlas.application.exception.TLAShortNameDoesNotExist;
import org.contextmapper.sample.tlas.application.exception.TLAShortNameNotValid;
import org.contextmapper.sample.tlas.domain.tla.ThreeLetterAbbreviation;
import org.contextmapper.sample.tlas.infrastructure.webapi.mapper.TlaApiDTOMapper;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.http.HttpStatusCode;

import java.util.function.Function;

@Component
public class GetTLAByNameHandler implements Function<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static Log logger = LogFactory.getLog(GetTLAByNameHandler.class);

    private TlaApplicationService service;
    private ObjectMapper objectMapper;
    private ResponseEventFactory responseFactory;

    public GetTLAByNameHandler(final TlaApplicationService service,
                               final ObjectMapper objectMapper,
                               final ResponseEventFactory responseFactory) {
        this.service = service;
        this.objectMapper = objectMapper;
        this.responseFactory = responseFactory;
    }

    @Override
    public APIGatewayProxyResponseEvent apply(APIGatewayProxyRequestEvent requestEvent) {
        try {
            String name = requestEvent.getPathParameters().get("name");
            ThreeLetterAbbreviation tla = service.getTLAByName(name);
            return responseFactory.createResponseEvent(objectMapper.writeValueAsString(TlaApiDTOMapper.tlaToDto(tla)));
        } catch (TLAShortNameDoesNotExist | TLAShortNameNotValid e) {
            logger.error("TLA not found", e);
            return responseFactory.createErrorResponseEvent(HttpStatusCode.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            logger.error("Internal server error", e);
            return responseFactory.createErrorResponseEvent(HttpStatusCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

}
