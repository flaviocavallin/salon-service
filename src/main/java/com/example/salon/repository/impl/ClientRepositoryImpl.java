package com.example.salon.repository.impl;

import com.example.salon.domain.Client;
import com.example.salon.domain.LoyaltyPoint;
import com.example.salon.domain.PointedClient;
import com.example.salon.repository.CustomClientRepository;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AccumulatorOperators;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationExpression;
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;

@Repository
class ClientRepositoryImpl implements CustomClientRepository {

    private final MongoTemplate mongoTemplate;

    ClientRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = Objects.requireNonNull(mongoTemplate, "mongoTemplate can not be null");
    }

    @Override
    public List<PointedClient> getTopMostLoyalActiveClientsBy(LocalDate dateFrom, LocalDate dateTo, int limit) {
        Objects.requireNonNull(dateFrom, "dateFrom can not be null");
        Objects.requireNonNull(dateTo, "dateTo can not be null");

        SortOperation sortByPointsDesc = sort(new Sort(Sort.Direction.DESC, "points"));
        LimitOperation limitOperation = limit(limit);
        AggregationExpression sumPointsExpression = AccumulatorOperators.Sum.sumOf("$loyaltyPoints.points");

        Aggregation aggregation = newAggregation(
                match(Criteria.where("banned").is(false)),
                project().andInclude("email")
                        .and(new AggregationExpression() {
                            @Override
                            public Document toDocument(AggregationOperationContext aggregationOperationContext) {
                                Document filterExpression = new Document();
                                filterExpression.put("input", "$loyaltyPoints");
                                filterExpression.put("as", "loyaltyPoint");
                                filterExpression.put("cond", new Document("$and", Arrays.asList(
                                        new Document("$gte", Arrays.asList("$$loyaltyPoint.date", dateFrom)),
                                        new Document("$lt", Arrays.asList("$$loyaltyPoint.date", dateTo.plusDays(1)))
                                )));
                                return new Document("$filter", filterExpression);
                            }
                        }).as("loyaltyPoints"),
                project().andInclude("email").and(sumPointsExpression).as("points"),
                sortByPointsDesc,
                limitOperation
        );


        AggregationResults<PointedClient> output
                = mongoTemplate.aggregate(aggregation, "clients", PointedClient.class);


        return output.getMappedResults();
    }

    @Override
    public void incrementLoyaltyPoints(String clientId, LocalDate date, long points) {
        Objects.requireNonNull(clientId, "clientId can not be null");
        Objects.requireNonNull(date, "date can not be null");

        Query query =
                new Query().addCriteria(Criteria.where("_id").is(clientId)
                        .andOperator(Criteria.where("loyaltyPoints.date").ne(date)));

        LoyaltyPoint loyaltyPoint = new LoyaltyPoint(date, points);
        Update update = new Update().addToSet("loyaltyPoints", loyaltyPoint);

        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, Client.class);

        if (updateResult.getModifiedCount() == 0) {
            query = new Query()
                    .addCriteria(Criteria.where("_id").is(clientId)
                            .andOperator(Criteria.where("loyaltyPoints.date").is(date)));

            update = new Update().inc("loyaltyPoints.$.points", points);
            mongoTemplate.updateFirst(query, update, Client.class);
        }
    }

}