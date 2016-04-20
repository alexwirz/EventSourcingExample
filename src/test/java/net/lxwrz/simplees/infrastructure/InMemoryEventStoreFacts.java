package net.lxwrz.simplees.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import junit.framework.Assert;
import net.lxwrz.simplees.bankaccount.BankAccount;
import net.lxwrz.simplees.bankaccount.Changes;
import net.lxwrz.simplees.bankaccount.MoneyDeposited;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

public class InMemoryEventStoreFacts {
    @Test
    public void canSaveEvents() throws VersionConflict, EventStreamNotFound, JsonProcessingException {
        EventStore inMemoryEventStore = new InMemoryEventStore();
        BankAccount bankAccount = new BankAccount();
        bankAccount.deposit(42);
        inMemoryEventStore.saveEvents(bankAccount.getId(), bankAccount.getUncommitedChanges(), 0);
        List<Event> reloaded = inMemoryEventStore.loadEvents(bankAccount.getId());
        Assert.assertNotNull(reloaded);
        List<MoneyDeposited> deposits = new Changes(reloaded).ofType(MoneyDeposited.class);
        Assert.assertEquals(deposits.get(0).amount, 42);
    }

    @Test
    public void appendsNewChangesToStoredEvents() throws VersionConflict, EventStreamNotFound, JsonProcessingException {
        EventStore inMemoryEventStore = new InMemoryEventStore();
        BankAccount bankAccount = new BankAccount();
        bankAccount.deposit(21);
        inMemoryEventStore.saveEvents(bankAccount.getId(), bankAccount.getUncommitedChanges(), 0);
        // reload
        List<Event> previousChanges = inMemoryEventStore.loadEvents(bankAccount.getId());
        BankAccount reloaded = new BankAccount();
        reloaded.loadFromHistory(previousChanges);
        // make more changes
        reloaded.deposit(21);
        inMemoryEventStore.saveEvents(bankAccount.getId(), bankAccount.getUncommitedChanges(), 1);
        List<Event> allChanges = inMemoryEventStore.loadEvents(bankAccount.getId());
        Assert.assertEquals(2, allChanges.size());
    }

    @Test(expected = VersionConflict.class)
    public void detectsVersionConflicts() throws VersionConflict, EventStreamNotFound, JsonProcessingException {
        EventStore inMemoryEventStore = new InMemoryEventStore();
        BankAccount bankAccount = new BankAccount();
        bankAccount.deposit(21);
        inMemoryEventStore.saveEvents(bankAccount.getId(), bankAccount.getUncommitedChanges(), 0);
        // reload
        List<Event> previousChanges = inMemoryEventStore.loadEvents(bankAccount.getId());
        BankAccount reloaded = new BankAccount();
        reloaded.loadFromHistory(previousChanges);
        // make more changes
        reloaded.deposit(21);
        // try to commit with same version -> should result in a version/merge conflict
        inMemoryEventStore.saveEvents(bankAccount.getId(), bankAccount.getUncommitedChanges(), 0);
    }

    @Test(expected = EventStreamNotFound.class)
    public void throwsWhenTryingToLoadUnkownStream() throws EventStreamNotFound {
        EventStore eventStore = new InMemoryEventStore();
        eventStore.loadEvents(UUID.randomUUID());
    }
}
