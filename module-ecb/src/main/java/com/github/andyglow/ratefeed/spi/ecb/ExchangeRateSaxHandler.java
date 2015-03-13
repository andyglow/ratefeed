package com.github.andyglow.ratefeed.spi.ecb;

import com.github.andyglow.ratefeed.ExchangeRate;
import com.github.andyglow.ratefeed.http.sax.ExchangeRateSaxHandlerBase;
import com.github.andyglow.ratefeed.util.IConsumer;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.time.format.DateTimeFormatter;

public class ExchangeRateSaxHandler extends ExchangeRateSaxHandlerBase {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private int depth = 0;

    public ExchangeRateSaxHandler(IConsumer<ExchangeRate> collector) {
        super(collector);
    }

//    private String print(Attributes attrs) {
//        StringBuilder sb = new StringBuilder("(");
//        for(int i=0; i<attrs.getLength(); i++) {
//            sb.append(attrs.getLocalName(i));
//            sb.append("=");
//            sb.append(attrs.getValue(i));
//            sb.append(",");
//        }
//        return sb.append(")").toString();
//    }

    @Override
    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
        depth ++;

        switch (name) {
            case "Cube":
                switch (depth) {
                    case 3:
                        setupDocumentDate(attributes, "time", formatter);
                        break;
                    case 4:
                        currentEntry = new Entry();
                        currentEntry.code = attributes.getValue("currency");
                        currentEntry.value = parseBigDecimal(attributes.getValue("rate"));
                        break;
                }
        }

    }

    @Override
    public void endElement(String uri, String localName, String name) throws SAXException {
        depth --;

        if(currentEntry==null) return;

        switch (name) {
            case "Cube":
                switch (depth) {
                    case 3:
                        if (documentDate != null)
                            handleItem(currentEntry.toExchangeRate(documentDate, RateProvider.HOME_CURRENCY));
                        currentEntry = null;
                        break;
                }
        }


    }

}
