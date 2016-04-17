package net.lxwrz.simplees.infrastructure;

import net.lxwrz.simplees.bankaccount.BankAccount;
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
}