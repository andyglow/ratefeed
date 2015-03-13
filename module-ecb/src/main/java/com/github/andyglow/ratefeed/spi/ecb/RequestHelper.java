package com.github.andyglow.ratefeed.spi.ecb;

import com.ning.http.client.RequestBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

class RequestHelper {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static RequestBuilder getRatesPath(LocalDate date) {
        return scriptURL(Optional.of(date));
    }

    private static RequestBuilder scriptURL(Optional<LocalDate> dateOpt) {
        RequestBuilder builder = new RequestBuilder("GET").setUrl(RateProvider.URL);
        return builder;
//        return dateOpt.map(date ->
//            builder.addQueryParam("time", date.format(formatter))
//        ).orElse(builder);
    }


}
