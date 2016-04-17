package net.lxwrz.simplees.infrastructure;

public class BankAccount extends AggregateRoot {
    private int balance;

    public void deposit(int amount) {
        super.applyChange(new MoneyDeposited(amount));
    }

    public void withdraw(int amount) {
        super.applyChange(new MoneyWithdrawn(amount));
    }

    public int getBalance() {
        return balance;
    }

    private void apply(MoneyDeposited deposit) {
        balance += deposit.amount;
    }

    private void apply(MoneyWithdrawn withdrawal) {
        balance -= withdrawal.amount;
    }
}
