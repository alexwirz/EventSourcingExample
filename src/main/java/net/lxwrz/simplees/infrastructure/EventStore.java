package net.lxwrz.simplees.infrastructure;

import java.util.List;
import java.util.UUID;

public interface EventStore {
    void saveEvents(UUID aggregateId, List<Event> events, long expectedVersion) throws VersionConflict;
    List<Event> loadEvents(UUID aggregateId) throws EventStreamNotFound;
}
