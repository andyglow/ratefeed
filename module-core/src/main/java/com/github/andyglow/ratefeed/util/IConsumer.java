package com.github.andyglow.ratefeed.util;

import java.util.Optional;

public interface IConsumer<U> {

    public void item(U item);
    public void done(Optional<Throwable> exOpt);

}
