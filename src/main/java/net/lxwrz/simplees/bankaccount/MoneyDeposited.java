package net.lxwrz.simplees.bankaccount;

import net.lxwrz.simplees.infrastructure.Event;

public class MoneyDeposited extends Event {
    public int amount;

    public MoneyDeposited(int number) {
        amount = number;
    }
}
