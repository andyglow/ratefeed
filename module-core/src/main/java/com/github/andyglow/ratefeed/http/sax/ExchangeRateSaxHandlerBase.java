/*
 *  Copyright (C) 2007 Cargosoft Incorporated. 
 *
 *  mercury-tools
 *  18.09.2009
 *
 *  $Licensetext$
 *
 */
package com.github.andyglow.ratefeed.http.sax;

import com.github.andyglow.ratefeed.ExchangeRate;
import com.github.andyglow.ratefeed.util.IConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Currency;

public abstract class ExchangeRateSaxHandlerBase extends SaxHandlerBase<ExchangeRate> {

    private static final Logger log = LoggerFactory.getLogger(ExchangeRateSaxHandler.class);

    public static class Entry {
        public String code;
        public BigDecimal value;
        public BigDecimal divider = BigDecimal.ONE;

        public ExchangeRate toExchangeRate(LocalDate date, Currency homeCurrency) {
            return new ExchangeRate(
                date,
                homeCurrency,
                Currency.getInstance(code),
                value.divide(divider, RoundingMode.HALF_UP)
            );
        }
    }

    /*
     * temporary buffer
     */
    protected StringBuilder sb = null;

    /*
     * Document Date. Must re equal to requested date
     */
    protected LocalDate documentDate = null;

    /*
     * Entry being processed currently
     */
    protected Entry currentEntry = null;

    public ExchangeRateSaxHandlerBase(IConsumer<ExchangeRate> collector) {
        super(collector);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (sb != null) sb.append(ch, start, length);
    }

    protected BigDecimal readBigDecimal() {
        return parseBigDecimal(sb.toString());
    }

    protected BigDecimal parseBigDecimal(String text) {
        text = text.replace(',', '.');
        try {
            return new BigDecimal(text);
        } catch(NumberFormatException nfe) {
            NumberFormatException ex = new NumberFormatException(text + ". " + nfe.getMessage());
            ex.initCause(nfe.getCause());
            throw ex;
        }
    }


    protected void setupDocumentDate(Attributes attributes, String attrName, DateTimeFormatter formatter) {
        String date = attributes.getValue(attrName);
        try {
            documentDate = LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            documentDate = null;
            log.warn("Error parse date {}", date, e);
        }
    }

}