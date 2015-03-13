package com.github.andyglow.ratefeed.spi.cbr.xml;

import com.github.andyglow.ratefeed.ExchangeRate;
import com.github.andyglow.ratefeed.http.HttpClient;
import com.github.andyglow.ratefeed.spi.IRateProvider;
import com.github.andyglow.ratefeed.spi.IRateProviderDescriptor;
import com.github.andyglow.ratefeed.http.sax.ExchangeRateSaxHandler;
import com.github.andyglow.ratefeed.http.sax.SaxParser;
import com.github.andyglow.ratefeed.util.CollectingAndDelegatingConsumer;
import com.github.andyglow.ratefeed.util.IConsumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Currency;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public final class XmlRateProvider implements IRateProvider, IRateProviderDescriptor {

    public static final Logger log = LoggerFactory.getLogger("currency-cbrw-xml");

    private static final ExchangeRateSaxHandler.Config handlerConfig = new ExchangeRateSaxHandler.Config(
        Currency.getInstance("RUB"),
        DateTimeFormatter.ofPattern("dd/MM/yyyy"),
        "Date",
        "ValCurs",
        "Valute",
        "CharCode",
        "Nominal",
        "Value"
    );

    @Override
    public CompletableFuture<Set<ExchangeRate>> getRates(
        Optional<IConsumer<ExchangeRate>> consumerOpt,
        LocalDate date) {

        return HttpClient.request(
            RequestHelper.getRatesPath(date)
        ).thenApplyAsync(response -> {
            CollectingAndDelegatingConsumer<ExchangeRate> consumer = new CollectingAndDelegatingConsumer<>(consumerOpt);
            SaxParser.parseSafe(response, new ExchangeRateSaxHandler(consumer, handlerConfig));
            return consumer.getCollectedItems();
        });
    }

    @Override
    public String getName() {
        return "CBR::XML";
    }

    @Override
    public IRateProvider create() {
        return this;
    }
}
