package net.lxwrz.simplees.infrastructure;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryEventStore implements EventStore {
    private class EventData {
        public Event event;
        public UUID id;
        public long version;

        public EventData(UUID id, long version, Event event) {
            this.id = id;
            this.version = version;
            this.event = event;
        }
    }

    private Map<UUID, List<EventData>> history = new HashMap<>();

    @Override
    public void saveEvents(UUID aggregateId, List<Event> events, long expectedVersion) throws VersionConflict {
        if(!history.containsKey(aggregateId)) {
            history.put(aggregateId, new ArrayList<>());
        }

        List<EventData> bucket = history.get(aggregateId);
        int storedEventsCount = bucket.size();
        long currentVersion = storedEventsCount > 0 ? bucket.get(storedEventsCount - 1).version : 0;
        if(currentVersion != expectedVersion) {
            throw new VersionConflict(expectedVersion, currentVersion);
        }

        for (Event event: events) {
            event.version = ++expectedVersion;
            bucket.add(new EventData(aggregateId, event.version, event));
        }
    }

    @Override
    public List<Event> loadEvents(UUID aggregateId) throws EventStreamNotFound {
        if(!history.containsKey(aggregateId)) throw new EventStreamNotFound();
        return history
                .get(aggregateId)
                .stream()
                .map((eventData) -> eventData.event)
                .collect(Collectors.toList());
    }
}
