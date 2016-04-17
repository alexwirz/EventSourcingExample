package net.lxwrz.simplees.bankaccount;

import net.lxwrz.simplees.infrastructure.AggregateRoot;
import net.lxwrz.simplees.infrastructure.Event;

import java.util.List;
import java.util.stream.Collectors;

public class Changes<T extends Event> {
    private List<Event> changes;

    public static Changes madeTo(AggregateRoot aggregateRoot) {
        return new Changes(aggregateRoot);
    }

    public Changes(AggregateRoot aggregateRoot) {
        this.changes = aggregateRoot.getUncommitedChanges();
    }

    public List<T> ofType(Class<T> type) {
        return changes.stream()
                .filter(type::isInstance)
                .map(type::cast)
                .collect(Collectors.toList());
    }
}
