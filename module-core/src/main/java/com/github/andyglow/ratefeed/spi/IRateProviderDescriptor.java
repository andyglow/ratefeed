package com.github.andyglow.ratefeed.spi;

public interface IRateProviderDescriptor {

    public String getName();

    public IRateProvider create();

}
