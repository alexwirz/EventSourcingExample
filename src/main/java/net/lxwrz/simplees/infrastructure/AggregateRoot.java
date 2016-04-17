package net.lxwrz.simplees.infrastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AggregateRoot {
    private UUID id;
    private List<Event> uncommitedChanges = new ArrayList<Event>();
    private long version;

    public AggregateRoot() {
        this(UUID.randomUUID());
    }

    public AggregateRoot(UUID aggregateId) {
        id = aggregateId;
    }

    public void applyChange(Event event) {
        UnimportantCrap.invokeHandlerMethod(this, event);
        uncommitedChanges.add(event);
    }

    public UUID getId() {
        return id;
    }

    public List<Event> getUncommitedChanges() {
        return uncommitedChanges;
    }

    public long getVersion() {
        return version;
    }

    public void loadFromHistory(List<Event> history) {
        for (Event event : history) {
            applyChangeFromHistory(event);
        }
    }

    private void applyChangeFromHistory(Event event) {
        UnimportantCrap.invokeHandlerMethod(this, event);
        version++;
    }

    public void markChangesAsCommited() {
        uncommitedChanges.clear();
    }
}
