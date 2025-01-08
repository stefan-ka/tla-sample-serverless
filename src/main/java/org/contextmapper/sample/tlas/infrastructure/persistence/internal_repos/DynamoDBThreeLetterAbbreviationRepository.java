package org.contextmapper.sample.tlas.infrastructure.persistence.internal_repos;

import org.contextmapper.sample.tlas.domain.tla.ThreeLetterAbbreviation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class DynamoDBThreeLetterAbbreviationRepository {

    private static final String TABLE_NAME = System.getenv("TLA_TABLE_NAME");
    private static final Logger logger = LoggerFactory.getLogger(DynamoDBThreeLetterAbbreviationRepository.class);

    private static final DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
            .credentialsProvider(DefaultCredentialsProvider.create())
            //.region(Region.EU_CENTRAL_1)
            .overrideConfiguration(ClientOverrideConfiguration.builder()
                    .build())
            .httpClient(UrlConnectionHttpClient.builder().build())
            .build();

    public Optional<ThreeLetterAbbreviation> findById(final String name) {
        GetItemResponse getItemResponse = dynamoDbClient.getItem(GetItemRequest.builder()
                .key(Map.of("name", AttributeValue.builder().s(name).build()))
                .tableName(TABLE_NAME)
                .build());
        if (getItemResponse.hasItem()) {
            return Optional.of(TLAMapper.tlaFromDynamoDB(getItemResponse.item()));
        } else {
            return Optional.empty();
        }
    }

    public List<ThreeLetterAbbreviation> findAll() {
        ScanResponse scanResponse = dynamoDbClient.scan(ScanRequest.builder()
                .tableName(TABLE_NAME)
                .build());

        logger.info("Scan returned: {} TLAs", scanResponse.count());

        return scanResponse.items().stream()
                .map(i -> TLAMapper.tlaFromDynamoDB(i))
                .collect(Collectors.toList());
    }

}
