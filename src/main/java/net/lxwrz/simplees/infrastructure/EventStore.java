package net.lxwrz.simplees.infrastructure;

import java.util.List;
import java.util.UUID;

public interface EventStore {
    void saveEvents(UUID aggregateId, List<Event> events, int expectedVersion) throws VersionConflict;
    List<Event> loadEvents(UUID aggregateId) throws EventStreamNotFound;
}
