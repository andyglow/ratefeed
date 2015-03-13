package com.github.andyglow.ratefeed;

import com.github.andyglow.ratefeed.http.HttpClient;
import com.github.andyglow.ratefeed.spi.IRateProviderDescriptor;

import java.util.Set;
import java.util.concurrent.CountDownLatch;

public class GrabRates {

    public static void main(String[] args) {

        // getting references to available providers
        Set<IRateProviderDescriptor> providers = RateFeed.getProviders();

        // setting up thread stopper
        CountDownLatch stopper = new CountDownLatch(providers.size());

        // run the thread collecting all the available rates
        new Thread(() -> {
            for (IRateProviderDescriptor d : providers) {
                final String prefix = d.getName();
                new RateFeed(d.create())
                    .current(r -> {

                        // noticing rate grabbed
                        System.out.println("* " + prefix + "::" + r);
                    }).thenAcceptAsync(rates -> {

                    // showing the total for selected provider
                    System.out.println(prefix + " loaded " + rates.size());

                    // and counting down the stopper
                    stopper.countDown();
                });
            }
        }).start();

        try {

            // wait for all feeds to become finished
            stopper.await();

            // stop the transport
            HttpClient.stop();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
