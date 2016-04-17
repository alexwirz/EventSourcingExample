package net.lxwrz.simplees.infrastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AggregateRoot {
    private UUID id;
    private List<Event> uncommitedChanges = new ArrayList<Event>();

    public AggregateRoot() {
        this(UUID.randomUUID());
    }

    public AggregateRoot(UUID aggregateId) {
        id = aggregateId;
    }

    public void applyChange(Event event) {
        applyChange(event, true);
    }

    private void applyChange(Event event, boolean isNew) {
        UnimportantCrap.invokeHandlerMethod(this, event);
        if (isNew) uncommitedChanges.add(event);
    }

    public List<Event> getUncommitedChanges() {
        return uncommitedChanges;
    }

    public void markChangesAsCommited() {
        uncommitedChanges.clear();
    }

    public void loadFromHistory(List<Event> history) {
        for (Event event : history) {
            applyChange(event, false);
        }
    }

    public UUID getId() {
        return id;
    }
}
