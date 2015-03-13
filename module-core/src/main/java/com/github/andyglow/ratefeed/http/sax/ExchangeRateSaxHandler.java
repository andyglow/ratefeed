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

import java.time.format.DateTimeFormatter;
import java.util.Currency;
import java.util.Objects;

public class ExchangeRateSaxHandler extends ExchangeRateSaxHandlerBase {

    private static final Logger log = LoggerFactory.getLogger(ExchangeRateSaxHandler.class);

    public static class Config {

        private Currency HomeCurrency;
        private DateTimeFormatter DateFormatter;
        private String DateAttribute;
        private String DocumentTag;
        private String EntryTag;
        private String CodeTag;
        private String DividerTag;
        private String ValueTag;

        public Config(Currency homeCurrency, DateTimeFormatter dateFormatter, String dateAttribute, String documentTag, String entryTag, String codeTag, String dividerTag, String valueTag) {
            HomeCurrency = Objects.requireNonNull(homeCurrency);
            DateFormatter = Objects.requireNonNull(dateFormatter);
            DateAttribute = Objects.requireNonNull(dateAttribute);
            DocumentTag = Objects.requireNonNull(documentTag);
            EntryTag = Objects.requireNonNull(entryTag);
            CodeTag = Objects.requireNonNull(codeTag);
            DividerTag = Objects.requireNonNull(dividerTag);
            ValueTag = Objects.requireNonNull(valueTag);
        }

    }

    private Config config;

    public ExchangeRateSaxHandler(IConsumer<ExchangeRate> collector, Config config) {
        super(collector);
        this.config = Objects.requireNonNull(config);
    }

    @Override
    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
        if(config.DocumentTag.equals(name)) {
            setupDocumentDate(attributes, config.DateAttribute, config.DateFormatter);
        } else if(config.EntryTag.equals(name)) {
            currentEntry = new Entry();
        } else if(config.CodeTag.equals(name) || config.DividerTag.equals(name) || config.ValueTag.equals(name)) {
            sb = new StringBuilder();
        }
    }


    @Override
    public void endElement(String uri, String localName, String name) throws SAXException {
        if(currentEntry==null) return;

        if(config.DocumentTag.equals(name)) {
            // skip
        } else if(config.EntryTag.equals(name)) {
            if(documentDate!=null) handleItem(currentEntry.toExchangeRate(documentDate, config.HomeCurrency));
            currentEntry = null;
        } else if(config.CodeTag.equals(name)) {
            currentEntry.code = sb.toString().trim();
        } else if(config.DividerTag.equals(name)) {
            currentEntry.divider = readBigDecimal();
        } else if(config.ValueTag.equals(name)) {
            currentEntry.value = readBigDecimal();
        }

    }

}