/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL-B
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info". 
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability. 
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or 
 * data to be ensured and,  more generally, to use and operate it in the 
 * same conditions as regards security. 
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.application.server.business.simulation.parser;

import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import java.io.FileReader;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * Parse a input file.
 *
 * This stores data in fields and this is not threadsafe. So it cannot be used
 * as a spring singleton and this needs prototype scope.
 *
 * @author Rafael Silva
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

    public String parse(String fileName) throws BusinessException {
        try {
            reader = XMLReaderFactory.createXMLReader();
            reader.setContentHandler(this);
            reader.parse(new InputSource(new FileReader(fileName)));

            return inputs.toString();

        } catch (IOException | SAXException ex) {
            logger.error("Error parsing file {}", fileName, ex);
            throw new BusinessException(ex);
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
