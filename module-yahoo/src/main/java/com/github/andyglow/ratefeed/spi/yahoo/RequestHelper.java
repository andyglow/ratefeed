package com.github.andyglow.ratefeed.spi.yahoo;

import com.ning.http.client.RequestBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Currency;
import java.util.Optional;

class RequestHelper {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String QUERY = "select * from yahoo.finance.xchange where pair in (" + buildPairList() + ")";

    private static String buildPairList() {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for(Currency c: RateProvider.TERM_CURRENCIES) {
            if(!first) sb.append(", ");
            sb.append("\"");
            sb.append(RateProvider.HOME_CURRENCY.getCurrencyCode());
            sb.append(c.getCurrencyCode());
            sb.append("\"");
            first = false;
        }
        return sb.toString();
    }

    public static RequestBuilder getRatesPath(LocalDate date) {
        return scriptURL(Optional.of(date));
    }

    private static RequestBuilder scriptURL(Optional<LocalDate> dateOpt) {
        RequestBuilder builder = new RequestBuilder("GET").setUrl(RateProvider.URL);
        builder.addQueryParam("format", "json");
        builder.addQueryParam("env", "store://datatables.org/alltableswithkeys");
        builder.addQueryParam("q", QUERY);
        return builder;
    }


}
