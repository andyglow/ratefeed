package com.github.andyglow.ratefeed.spi.ecb;

import com.github.andyglow.ratefeed.ExchangeRate;
import com.github.andyglow.ratefeed.http.HttpClient;
import com.github.andyglow.ratefeed.http.sax.SaxParser;
import com.github.andyglow.ratefeed.spi.IRateProvider;
import com.github.andyglow.ratefeed.spi.IRateProviderDescriptor;
import com.github.andyglow.ratefeed.util.CollectingAndDelegatingConsumer;
import com.github.andyglow.ratefeed.util.IConsumer;

import java.time.LocalDate;
import java.util.Currency;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public final class RateProvider implements IRateProvider, IRateProviderDescriptor {

    public static final Currency HOME_CURRENCY = Currency.getInstance("EUR");

    static final String URL = "http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";

    @Override
    public CompletableFuture<Set<ExchangeRate>> getRates(
        Optional<IConsumer<ExchangeRate>> consumerOpt,
        LocalDate date) {

        return HttpClient.request(
            RequestHelper.getRatesPath(date)
        ).thenApplyAsync(response -> {
            CollectingAndDelegatingConsumer<ExchangeRate> consumer = new CollectingAndDelegatingConsumer<>(consumerOpt);
            SaxParser.parseSafe(response, new ExchangeRateSaxHandler(consumer));
            return consumer.getCollectedItems();
        });
    }

    @Override
    public String getName() {
        return "ECB";
    }

    @Override
    public IRateProvider create() {
        return this;
    }

}
