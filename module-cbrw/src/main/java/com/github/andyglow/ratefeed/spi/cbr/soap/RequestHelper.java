package com.github.andyglow.ratefeed.spi.cbr.soap;

import com.ning.http.client.RequestBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.github.andyglow.ratefeed.spi.cbr.Constants.HOST;
import static com.github.andyglow.ratefeed.spi.cbr.Constants.WS_PATH;

class RequestHelper {

    public static final String GET_RATES_PAYLOAD_TEMPLATE = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
        "<soap:Envelope\n" +
        "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
        "    xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n" +
        "    xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
        "    <soap:Body>\n" +
        "        <GetCursOnDateXML xmlns=\"http://web.cbr.ru/\">\n" +
        "            <On_date>%s</On_date>\n" +
        "        </GetCursOnDateXML>\n" +
        "    </soap:Body>\n" +
        "</soap:Envelope>";

    private static final String SCRIPT_URL = String.format("http://%s%s", HOST, WS_PATH);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    public static RequestBuilder getRatesPath(LocalDate date) {
        return scriptURL(date);
    }

    private static RequestBuilder scriptURL(LocalDate date) {
        RequestBuilder builder = new RequestBuilder("POST").setUrl(SCRIPT_URL);
        builder.addHeader("Host", "www.cbr.ru");
        builder.addHeader("Content-Type", "application/soap+xml; charset=utf-8");
        builder.setBody(createPayload(date));
        return builder;
    }

    private static String createPayload(LocalDate date) {
        return String.format(GET_RATES_PAYLOAD_TEMPLATE, date.format(formatter));
    }

}
