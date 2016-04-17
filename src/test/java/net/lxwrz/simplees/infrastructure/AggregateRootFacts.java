package net.lxwrz.simplees.infrastructure;

import net.lxwrz.simplees.bankaccount.BankAccount;
import net.lxwrz.simplees.bankaccount.MoneyDeposited;
import net.lxwrz.simplees.bankaccount.MoneyWithdrawn;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class AggregateRootFacts {
    @Test
    public void applyAccumulatesUncommitedChanges() {
        AggregateRoot aggregateRoot = new AggregateRoot();
        MoneyDeposited event = new MoneyDeposited(42);
        aggregateRoot.applyChange(event);
        List<Event> uncommitedChanges = aggregateRoot.getUncommitedChanges();
        Assert.assertNotNull("uncommited changes must not be null", uncommitedChanges);
        Assert.assertEquals("must containi previously applied change", uncommitedChanges.get(0), event);
    }

    @Test
    public void applyDispatchesEventsToSubclasses() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.deposit(42);
        Assert.assertEquals("deposit event applied", bankAccount.getBalance(), 42);
    }

    @Test
    public void markChangesAsCommitedClearsUncommitedChanges() {
        AggregateRoot aggregateRoot = new AggregateRoot();
        aggregateRoot.applyChange(new MoneyDeposited(42));
        aggregateRoot.markChangesAsCommited();
        List<Event> uncommitedChanges = aggregateRoot.getUncommitedChanges();
        Assert.assertTrue("unciommited changes cleared after mark as completed", uncommitedChanges.isEmpty());
    }

    @Test
    public void loadsAggregateFromHistory() {
        BankAccount bankAccount = new BankAccount();
        List<Event> history = new ArrayList<Event>();
        history.add(new MoneyDeposited(2));
        history.add(new MoneyDeposited(3));
        history.add(new MoneyWithdrawn(1));
        bankAccount.loadFromHistory(history);
        Assert.assertEquals("aggregate didn't load the history", bankAccount.getBalance(), 4);
    }
}