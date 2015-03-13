package com.github.andyglow.ratefeed.http.sax;

import com.github.andyglow.ratefeed.util.IConsumer;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Optional;

public abstract class SaxHandlerBase<U> extends DefaultHandler {

    private IConsumer<U> collector;

    public SaxHandlerBase(IConsumer<U> collector) {
        this.collector = collector;
    }

    protected final void handleItem(U item) {
        collector.item(item);
    }

    @Override
    public final void endDocument() throws SAXException {
        collector.done(Optional.empty());
    }

}
