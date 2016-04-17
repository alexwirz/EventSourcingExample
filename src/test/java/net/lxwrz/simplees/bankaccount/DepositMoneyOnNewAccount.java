package net.lxwrz.simplees.bankaccount;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class DepositMoneyOnNewAccount {
    private BankAccount bankAccount = new BankAccount();

    @Before
    public void When() {
        bankAccount.deposit(42);
    }

    @Test
    public void ThenExactlyOneDepositIsMade() {
        List<MoneyDeposited> deposits = Changes.madeTo(bankAccount).ofType(MoneyDeposited.class);
        Assert.assertEquals(1, deposits.size());
    }

    @Test
    public void ThenGivenAmountCreditedToNewAccount() {
        List<MoneyDeposited> deposits = Changes.madeTo(bankAccount).ofType(MoneyDeposited.class);
        int totalDepositAmount = deposits
                .stream()
                .mapToInt((deposit) -> deposit.amount)
                .sum();
        Assert.assertEquals(42, totalDepositAmount);
    }
}
