package com.github.andyglow.ratefeed.spi.cbr.xml;

import com.ning.http.client.RequestBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.github.andyglow.ratefeed.spi.cbr.Constants.HOST;
import static com.github.andyglow.ratefeed.spi.cbr.Constants.SCRIPT_PATH;

class RequestHelper {

    private static final String SCRIPT_URL = String.format("http://%s%s", HOST, SCRIPT_PATH);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static RequestBuilder getRatesPath(LocalDate date) {
        return scriptURL(Optional.of(date));
    }

    private static RequestBuilder scriptURL(Optional<LocalDate> dateOpt) {
        RequestBuilder builder = new RequestBuilder("GET").setUrl(SCRIPT_URL);
        return dateOpt.map(date ->
                builder.addQueryParam("date_req", date.format(formatter))
        ).orElse(builder);
    }


}
