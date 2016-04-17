package net.lxwrz.simplees.bankaccount;

import net.lxwrz.simplees.infrastructure.Event;

public class MoneyWithdrawn extends Event {
    public int amount;

    public MoneyWithdrawn(int amount) {
        this.amount = amount;
    }
}
