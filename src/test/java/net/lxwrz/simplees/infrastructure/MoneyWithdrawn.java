package net.lxwrz.simplees.infrastructure;

public class MoneyWithdrawn extends Event {
    public int amount;

    public MoneyWithdrawn(int amount) {
        this.amount = amount;
    }
}
