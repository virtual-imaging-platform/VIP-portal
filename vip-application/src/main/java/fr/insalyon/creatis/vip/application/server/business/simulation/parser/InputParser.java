package fr.insalyon.creatis.vip.application.server.business.simulation.parser;

import java.io.FileReader;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import fr.insalyon.creatis.vip.core.client.VipException;

/**
 * Parse a input file.
 *
 * This stores data in fields and this is not threadsafe. So it cannot be used
 * as a spring singleton and this needs prototype scope.
 */
@Service
@Scope("prototype")
public class InputParser extends DefaultHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private XMLReader reader;
    private StringBuilder inputs;
    private String name;
    private String value;

    public InputParser() {
        inputs = new StringBuilder();
    }

    public String parse(String fileName) throws VipException {
        try {
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            parserFactory.setNamespaceAware(true);
            reader = parserFactory.newSAXParser().getXMLReader();
            reader.setContentHandler(this);
            reader.parse(new InputSource(new FileReader(fileName)));

            return inputs.toString();

        } catch (IOException | SAXException | ParserConfigurationException ex) {
            logger.error("Error parsing file {}", fileName, ex);
            throw new VipException(ex);
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        if (localName.equals("PARAMETER_LIST")) {
            name = attributes.getValue("name");
            return;
        }
        if (localName.equals("PARAMETER_RANGE")) {
            name = attributes.getValue("name");
            return;
        }
        if (localName.equals("START")) {
            value = "Start: ";
            return;
        }
        if (localName.equals("END")) {
            value += " - Stop: ";
            return;
        }
        if (localName.equals("STEP")) {
            value += " - Step: ";
            return;
        }
        if (localName.equals("VAL")) {
            if (value == null || value.isEmpty()) {
                value = "";
            } else {
                value += "; ";
            }
            return;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {

        if (localName.equals("PARAMETER_LIST")
                || localName.equals("PARAMETER_RANGE")) {

            inputs.append(name);
            inputs.append(" = ");
            inputs.append(value);
            inputs.append("<br />");
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
