package org.contextmapper.sample.tlas.infrastructure.webapi;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.contextmapper.sample.tlas.application.TlaGroupsApplicationService;
import org.contextmapper.sample.tlas.application.exception.TLAGroupShortNameDoesNotExist;
import org.contextmapper.sample.tlas.application.exception.TLAGroupShortNameNotValid;
import org.contextmapper.sample.tlas.domain.tla.TLAGroup;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.http.HttpStatusCode;

import java.util.function.Function;

import static org.contextmapper.sample.tlas.infrastructure.webapi.mapper.TlaApiDTOMapper.tlaGroupToDto;

@Component
public class GetTLAGroupByNameHandler implements Function<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final Log logger = LogFactory.getLog(GetTLAGroupByNameHandler.class);

    private final TlaGroupsApplicationService service;
    private final ObjectMapper objectMapper;
    private final ResponseEventFactory responseFactory;

    public GetTLAGroupByNameHandler(final TlaGroupsApplicationService service,
                                    final ObjectMapper objectMapper,
                                    final ResponseEventFactory responseFactory) {
        this.service = service;
        this.objectMapper = objectMapper;
        this.responseFactory = responseFactory;
    }

    @Override
    public APIGatewayProxyResponseEvent apply(APIGatewayProxyRequestEvent requestEvent) {
        try {
            String name = requestEvent.getPathParameters().get("groupName");
            TLAGroup tlaGroup = service.getGroupByName(name);
            return responseFactory.createResponseEvent(objectMapper.writeValueAsString(tlaGroupToDto(tlaGroup)));
        } catch (TLAGroupShortNameDoesNotExist | TLAGroupShortNameNotValid e) {
            logger.error("TLA group not found", e);
            return responseFactory.createErrorResponseEvent(HttpStatusCode.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            logger.error("Internal server error", e);
            return responseFactory.createErrorResponseEvent(HttpStatusCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

}
