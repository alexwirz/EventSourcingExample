package net.lxwrz.simplees.bankaccount;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import net.lxwrz.simplees.infrastructure.Event;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class MoneyWithdrawn extends Event {
    public int amount;

    public MoneyWithdrawn() {}

    public MoneyWithdrawn(int amount) {
        this.amount = amount;
    }
}
