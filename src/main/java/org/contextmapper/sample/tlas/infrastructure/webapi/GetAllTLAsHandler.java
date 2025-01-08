package org.contextmapper.sample.tlas.infrastructure.webapi;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.contextmapper.sample.tlas.application.TlaApplicationService;
import org.contextmapper.sample.tlas.infrastructure.webapi.mapper.TlaApiDTOMapper;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.http.HttpStatusCode;

import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class GetAllTLAsHandler implements Function<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static Log logger = LogFactory.getLog(GetAllTLAsHandler.class);

    private TlaApplicationService service;
    private ObjectMapper objectMapper;
    private ResponseEventFactory responseFactory;

    public GetAllTLAsHandler(final TlaApplicationService service,
                             final ObjectMapper objectMapper,
                             final ResponseEventFactory responseFactory) {
        this.service = service;
        this.objectMapper = objectMapper;
        this.responseFactory = responseFactory;
    }

    @Override
    public APIGatewayProxyResponseEvent apply(APIGatewayProxyRequestEvent requestEvent) {
        logger.info("GetAllTLAsHandler called");
        try {
            var tlas = service.findAllTLAs().stream()
                    .map(TlaApiDTOMapper::tlaToDto)
                    .collect(Collectors.toList());
            logger.info("GetAllTLAsHandler returning " + tlas.size() + " TLAs");
            return responseFactory.createResponseEvent(objectMapper.writeValueAsString(tlas));
        } catch (Exception e) {
            logger.error("Internal error has happened", e);
            return new APIGatewayProxyResponseEvent().withStatusCode(HttpStatusCode.INTERNAL_SERVER_ERROR)
                    .withBody("Internal error has happened: " + e.getMessage());
        }

    }

}
