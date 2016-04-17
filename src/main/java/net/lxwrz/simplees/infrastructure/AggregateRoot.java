package net.lxwrz.simplees.infrastructure;

import java.util.ArrayList;
import java.util.List;

public class AggregateRoot {
    private List<Event> uncommitedChanges = new ArrayList<Event>();

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
        for (Event event :
                history) {
            applyChange(event, false);
        }
    }
}
