package fr.insalyon.creatis.vip.gatelab.server.business;

import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

/**
 * Parse a gatelab input file.
 *
 * This stores data in fields and this is not threadsafe. So it cannot be used
 * as a spring singleton and this needs prototype scope.
 *
 * @author Ibrahim Kallel, Rafael Silva
 */
@Component
@Scope("prototype")
public class GateLabInputsParser extends DefaultHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private XMLReader reader;
    private Map<String, String> inputsMap;
    private String name;
    private String value;

    public GateLabInputsParser() {
        inputsMap = new HashMap<String, String>();
    }

    public Map<String, String> parse(String fileName) {
        try {
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            parserFactory.setNamespaceAware(true);
            reader = parserFactory.newSAXParser().getXMLReader();
            reader.setContentHandler(this);
            reader.parse(new InputSource(new FileReader(fileName)));

            return inputsMap;

        } catch (IOException | SAXException | ParserConfigurationException ex) {
            logger.error("Error parsing {}", fileName, ex);
        }
        return null;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        if (localName.equals("source")) {
            name = attributes.getValue("name");
            return;
        }
        if (localName.equals("item")) {
            value = "";
            return;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if (localName.equals("item")) {
            inputsMap.put(name, value);
            value = null;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (value != null) {
            String chars = new String(ch);
            value += chars.substring(start, start + length);
        }
    }
}
