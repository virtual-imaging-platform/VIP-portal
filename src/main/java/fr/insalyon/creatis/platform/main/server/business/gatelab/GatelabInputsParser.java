/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.insalyon.creatis.platform.main.server.business.gatelab;

/**
 *
 * @author ibrahim
 */
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 *
 * @author Ibrahim Kallel
 */
public class GatelabInputsParser extends DefaultHandler {

    private XMLReader reader;
    private Map<String, String> inputsMap;
    private String name;
    private String value;
    

    public GatelabInputsParser() {
        inputsMap = new HashMap<String, String>();
    }

    public Map<String, String> parse(String fileName) {
        try {
            reader = XMLReaderFactory.createXMLReader();
            reader.setContentHandler(this);
            reader.parse(new InputSource(new FileReader(fileName)));

            return inputsMap;

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (SAXException ex) {
            ex.printStackTrace();
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
            value="";
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
            value += chars.substring(start,start+length);
        }
    }
}

