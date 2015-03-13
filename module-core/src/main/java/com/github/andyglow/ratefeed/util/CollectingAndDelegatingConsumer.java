package com.github.andyglow.ratefeed.util;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class CollectingAndDelegatingConsumer<U> implements IConsumer<U> {

    private Set<U> collectedItems;
    private Optional<IConsumer<U>> delegateOpt;

    public CollectingAndDelegatingConsumer(Optional<IConsumer<U>> delegateOpt) {
        this.delegateOpt = delegateOpt;
        collectedItems = new HashSet<>(16);
    }

    @Override
    public void item(U item) {
        collectedItems.add(item);
        delegateOpt.ifPresent((c) -> c.item(item));
    }

    @Override
    public void done(Optional<Throwable> exOpt) {
        delegateOpt.ifPresent((c) -> c.done(exOpt));
    }

    public Set<U> getCollectedItems() {
        return collectedItems;
    }

}
