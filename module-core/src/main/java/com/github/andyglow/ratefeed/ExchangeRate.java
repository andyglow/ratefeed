/*
 *  Copyright (C) 2007 Cargosoft Incorporated. 
 *
 *  mercury-tools
 *  18.09.2009
 *
 *  $Licensetext$
 *
 */
package com.github.andyglow.ratefeed;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

public class ExchangeRate {

    private LocalDate date;
    private Currency base;
    private Currency term;
    private BigDecimal value;

    public ExchangeRate(LocalDate date, Currency base, Currency term, BigDecimal value) {
        this.date = date;
        this.base = base;
        this.term = term;
        this.value = value;
    }

    public LocalDate getDate() {
        return date;
    }

    public Currency getBase() {
        return base;
    }

    public Currency getTerm() {
        return term;
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "ExchangeRate(" +
            "date=" + date +
            ", base=" + base +
            ", term=" + term +
            ", value=" + value +
        ')';
    }
}
