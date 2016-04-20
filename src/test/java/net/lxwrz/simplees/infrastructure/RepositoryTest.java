package net.lxwrz.simplees.infrastructure;

import net.lxwrz.simplees.bankaccount.BankAccount;
import net.lxwrz.simplees.bankaccount.InsufficientFunds;
import org.junit.Test;

import static org.junit.Assert.*;

public class RepositoryTest {
    @Test
    public void canLoadSavedAggregates() throws Exception, VersionConflict, EventStreamNotFound {
        BankAccount bankAccount = new BankAccount();
        bankAccount.deposit(42);
        Repository<BankAccount> repository = new Repository<>(new InMemoryEventStore());
        repository.save(bankAccount);
        BankAccount reloadedAccount = repository.findById(bankAccount.getId(), BankAccount.class);
        assertNotNull(reloadedAccount);
        assertEquals(42, reloadedAccount.getBalance());
    }

    @Test
    public void canUseMongo() throws Exception, VersionConflict, EventStreamNotFound, InsufficientFunds {
        BankAccount bankAccount = new BankAccount();
        bankAccount.deposit(42);
        bankAccount.deposit(42);
        bankAccount.withdraw(42);
        bankAccount.deposit(42);
        Repository<BankAccount> repository = new Repository<>(new MongoEventStore("test", "testEvents"));
        repository.save(bankAccount);
        BankAccount reloadedAccount = repository.findById(bankAccount.getId(), BankAccount.class);
        assertNotNull(reloadedAccount);
        assertEquals(84, reloadedAccount.getBalance());
    }
}