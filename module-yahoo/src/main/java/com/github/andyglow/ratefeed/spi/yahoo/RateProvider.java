package com.github.andyglow.ratefeed.spi.yahoo;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.andyglow.ratefeed.ExchangeRate;
import com.github.andyglow.ratefeed.http.HttpClient;
import com.github.andyglow.ratefeed.http.sax.SaxParser;
import com.github.andyglow.ratefeed.spi.IRateProvider;
import com.github.andyglow.ratefeed.spi.IRateProviderDescriptor;
import com.github.andyglow.ratefeed.util.CollectingAndDelegatingConsumer;
import com.github.andyglow.ratefeed.util.IConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Currency;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public final class RateProvider implements IRateProvider, IRateProviderDescriptor {

    private static final Logger log = LoggerFactory.getLogger(RateProvider.class);
    public static final Currency HOME_CURRENCY = Currency.getInstance("USD");
    public static final Set<Currency> TERM_CURRENCIES = Currency.getAvailableCurrencies();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
    static final String URL = "http://query.yahooapis.com/v1/public/yql";

    @Override
    public CompletableFuture<Set<ExchangeRate>> getRates(
        Optional<IConsumer<ExchangeRate>> consumerOpt,
        LocalDate date) {

        return HttpClient.request(
            RequestHelper.getRatesPath(date)
        ).thenApplyAsync(response -> {
            CollectingAndDelegatingConsumer<ExchangeRate> consumer = new CollectingAndDelegatingConsumer<>(consumerOpt);
            try {

                // TODO: use jackson straming api
                ObjectMapper om = new ObjectMapper();
                JsonNode root = om.readTree(response.getResponseBodyAsStream());
                JsonNode rates = root.path("query").path("results").path("rate");
                for(int i=0; i<rates.size(); i++) {
                    JsonNode e = rates.get(i);
                    try {
                        ExchangeRate er = new ExchangeRate(
                            LocalDate.parse(e.path("Date").textValue(), formatter),
                            HOME_CURRENCY,
                            Currency.getInstance(e.path("Name").textValue().substring(4)),
                            new BigDecimal(e.path("Rate").textValue())
                        );
                        consumer.item(er);
                    } catch(DateTimeParseException pe) {
                        // skip
                    }
                }
                consumer.done(Optional.empty());
            } catch (Throwable th) {
                log.warn("Unable to parse response", th);
                consumer.done(Optional.of(th));
            }

            return consumer.getCollectedItems();
        });
    }

    @Override
    public String getName() {
        return "YAHOO";
    }

    @Override
    public IRateProvider create() {
        return this;
    }

}
