package com.github.andyglow.ratefeed.spi;

import com.github.andyglow.ratefeed.ExchangeRate;
import com.github.andyglow.ratefeed.util.IConsumer;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public interface IRateProvider {

    default public CompletableFuture<Set<ExchangeRate>> getRates(Consumer<ExchangeRate> consumer, LocalDate date) {
        return getRates(
            Optional.of(new PartialConsumer<>(consumer)),
            date
        );
    }

    public CompletableFuture<Set<ExchangeRate>> getRates(
        Optional<IConsumer<ExchangeRate>> collectorOpt,
        LocalDate date);

    static class PartialConsumer<U> implements IConsumer<U> {
        private Consumer<U> consumer;

        public PartialConsumer(Consumer<U> consumer) {
            this.consumer = consumer;
        }

        @Override
        public void item(U item) {
            consumer.accept(item);
        }

        @Override
        public void done(Optional<Throwable> exOpt) {
        }

    }

}
