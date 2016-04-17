package net.lxwrz.simplees.infrastructure;

public class MoneyDeposited extends Event {
    public int amount;

    public MoneyDeposited(int number) {
        amount = number;
    }
}
