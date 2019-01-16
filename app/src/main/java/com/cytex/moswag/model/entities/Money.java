/*
 * Copyright (c) 2018. http://moswag@cytex.com- All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * If you use or distribute this project then you MUST ADD A COPY OF LICENCE
 * along with the project.
 *  Written by Webster Moswa <webstermoswa11@Gmail.com>, 2018.
 */

package com.cytex.moswag.model.entities;


import java.math.RoundingMode;
import java.util.Currency;
import java.util.Locale;

public class Money {

    private static final Currency INR = Currency.getInstance(new Locale("en",
            "US"));
    private static final RoundingMode DEFAULT_ROUNDING = RoundingMode.HALF_EVEN;

    private Double amount;
    private Currency currency;

    Money(Double amount, Currency currency) {
        this(amount, currency, DEFAULT_ROUNDING);
    }

    Money(Double amount, Currency currency, RoundingMode rounding) {
        this.amount = amount;
        this.currency = currency;
    }

    public static Money rupees(Double amount) {
        return new Money(amount, INR);
    }

    public Double getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    @Override
    public String toString() {
        return getCurrency().getSymbol() + " " + getAmount();
    }

    public String toString(Locale locale) {
        return getCurrency().getSymbol(locale) + " " + getAmount();
    }
}