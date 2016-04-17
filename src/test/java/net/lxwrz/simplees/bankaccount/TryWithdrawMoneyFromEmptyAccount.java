package net.lxwrz.simplees.bankaccount;

import net.lxwrz.simplees.infrastructure.Event;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.*;

public class TryWithdrawMoneyFromEmptyAccount {
    private BankAccount emptyBankAccount = new BankAccount();

    @Before
    public void When() {
        try {
            emptyBankAccount.withdraw(42);
        } catch (Throwable error){
        }
    }

    @Test
    public void ThenNoChangesMade() {
        List<Event> changes = emptyBankAccount.getUncommitedChanges();
        assertTrue("No changes should have been made to the bank account", changes.isEmpty());
    }
}
