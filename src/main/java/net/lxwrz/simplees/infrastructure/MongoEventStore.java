package net.lxwrz.simplees.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MongoEventStore implements EventStore {
    private String databaseName;
    private String collectionName;

    public MongoEventStore(String databaseName, String collectionName) {
        this.databaseName = databaseName;
        this.collectionName = collectionName;
    }

    @Override
    public void saveEvents(UUID aggregateId, List<Event> events, long expectedVersion) throws VersionConflict, JsonProcessingException {
        MongoDatabase db = getMongoDatabase();
        MongoCollection eventsCollection = db.getCollection(collectionName);
        eventsCollection.insertMany(toDocuments(aggregateId, events, expectedVersion));
    }

    private MongoDatabase getMongoDatabase() {
        MongoClient mongoClient = new MongoClient();
        return mongoClient.getDatabase(databaseName);
    }

    private List<Document> toDocuments(UUID aggregateId, List<Event> events, long version) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<Document> documents = new ArrayList<>();
        for (Event event :
                events) {
            documents.add(new Document()
                    .append("AggregateId", aggregateId)
                    .append("EventData", mapper.writeValueAsString(event))
                    .append("Version", ++version));
        }

        return documents;
    }

    @Override
    public List<Event> loadEvents(UUID aggregateId) throws EventStreamNotFound {
        FindIterable<Document> iterable = getMongoDatabase()
                .getCollection(collectionName)
                .find(new Document("AggregateId", aggregateId));
        final List<Event> events = new ArrayList<>();
        iterable.forEach((Block<Document>) document -> {
            try {
                events.add((Event) deserialize(document.get("EventData").toString()));
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        });

        return events;
    }

    private Object deserialize(String json) throws ClassNotFoundException, IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final JsonNode jsonNode = mapper.readTree(json);
        final String typeName = jsonNode.get("@type").asText();
        final Class<?> type = Class.forName(typeName);
        return mapper.convertValue(jsonNode, type);
    }
}
