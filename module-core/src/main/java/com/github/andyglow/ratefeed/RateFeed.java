package com.github.andyglow.ratefeed;

import com.github.andyglow.ratefeed.spi.IRateProvider;
import com.github.andyglow.ratefeed.spi.IRateProviderDescriptor;
import com.github.andyglow.ratefeed.util.IConsumer;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class RateFeed {

    private IRateProvider source;

    public RateFeed(IRateProvider source) {
        this.source = source;
    }

    public static Set<IRateProviderDescriptor> getProviders() {
        HashSet<IRateProviderDescriptor> descriptors = new HashSet<>();
        ServiceLoader.load(IRateProviderDescriptor.class).forEach(descriptors::add);
        return descriptors;
    }

    public CompletableFuture<Set<ExchangeRate>> from(LocalDate date) {
        return source.getRates(Optional.empty(), date);
    }

    public CompletableFuture<Set<ExchangeRate>> from(Consumer<ExchangeRate> consumer, LocalDate date) {
        return source.getRates(consumer, date);
    }

    public CompletableFuture<Set<ExchangeRate>> from(IConsumer<ExchangeRate> consumer, LocalDate date) {
        return source.getRates(Optional.of(consumer), date);
    }


    public CompletableFuture<Set<ExchangeRate>> current() {
        return from(LocalDate.now());
    }

    public CompletableFuture<Set<ExchangeRate>> current(Consumer<ExchangeRate> consumer) {
        return from(consumer, LocalDate.now());
    }

    public CompletableFuture<Set<ExchangeRate>> current(IConsumer<ExchangeRate> consumer) {
        return from(consumer, LocalDate.now());
    }

}
