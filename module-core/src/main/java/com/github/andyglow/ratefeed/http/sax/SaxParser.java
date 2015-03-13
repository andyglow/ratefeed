package com.github.andyglow.ratefeed.http.sax;

import com.ning.http.client.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class SaxParser {

    private static final Logger log = LoggerFactory.getLogger(SaxParser.class);

    private static final SAXParserFactory factory = SAXParserFactory.newInstance();

    public static void parseSafe(Response resp, DefaultHandler dh) {
        try {
            SAXParser parser = factory.newSAXParser();
            parser.parse(resp.getResponseBodyAsStream(), dh);
        } catch (Throwable th) {
            log.warn("Unable to parse response", th);
        }
    }

}
