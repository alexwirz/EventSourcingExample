package net.lxwrz.simplees.infrastructure;

import java.util.List;
import java.util.UUID;

public class Repository<T extends AggregateRoot> {
    private EventStore storage;

    public Repository(EventStore storage) {
        this.storage = storage;
    }

    public void save(T aggregate) throws Exception, VersionConflict {
        storage.saveEvents(
                aggregate.getId(),
                aggregate.getUncommitedChanges(),
                aggregate.getVersion());
        aggregate.markChangesAsCommited();
    }

    public T findById(UUID id, Class<T> type) throws EventStreamNotFound, IllegalAccessException, InstantiationException {
        List<Event> eventStream = storage.loadEvents(id);
        T aggregateRoot = type.newInstance();
        aggregateRoot.loadFromHistory(eventStream);
        return aggregateRoot;
    }
}
