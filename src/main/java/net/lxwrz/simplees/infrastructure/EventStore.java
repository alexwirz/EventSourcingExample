package net.lxwrz.simplees.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.UUID;

public interface EventStore {
    void saveEvents(UUID aggregateId, List<Event> events, long expectedVersion) throws VersionConflict, JsonProcessingException;
    List<Event> loadEvents(UUID aggregateId) throws EventStreamNotFound;
}
