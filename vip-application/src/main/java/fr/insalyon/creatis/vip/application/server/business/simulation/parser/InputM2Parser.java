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
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.insalyon.creatis.vip.datamanager.server.business.LfcPathsBusiness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

/**
 * Parse a m2 input file.
 *
 * This stores data in fields and this is not threadsafe. So it cannot be used
 * as a spring singleton and this needs prototype scope.
 *
 * @author Rafael Silva
 */
@Service
@Scope("prototype")
public class InputM2Parser extends DefaultHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private boolean parsingItem;
    private Map<String, String> inputs;
    private List<String> values;
    private String currentUserFolder;
    private String name;
    private StringBuilder itemContent;

    private LfcPathsBusiness lfcPathsBusiness;

    @Autowired
    public final void setLfcPathsBusiness(LfcPathsBusiness lfcPathsBusiness) {
        this.lfcPathsBusiness = lfcPathsBusiness;
    }

    public InputM2Parser() {
        this(null);
    }

    public InputM2Parser(String currentUserFolder) {
        this.inputs = new HashMap<String, String>();
        this.parsingItem = false;
        this.currentUserFolder = currentUserFolder;
    }

    public Map<String, String> parse(String fileName)
            throws BusinessException {

        try {
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            parserFactory.setNamespaceAware(true);
            XMLReader reader = parserFactory.newSAXParser().getXMLReader();
            reader.setContentHandler(this);
            reader.parse(new InputSource(new FileReader(fileName)));

            return inputs;

        } catch (IOException | SAXException | ParserConfigurationException ex) {
            logger.error("Error parsing {}", fileName, ex);
            throw new BusinessException(ex);
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {

        if (localName.equals("source")) {
            name = attributes.getValue("name");
            values = new ArrayList<String>();

        } else if (localName.equals("item")) {
            parsingItem = true;
            itemContent = new StringBuilder();
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        boolean isList = false;
        double step = -1;
        double firstValue = -1;
        double lastValue = -1;

        if (localName.equals("source")) {
            if (values.size() == 1) {
                String path = values.get(0);
                try {
                    if (lfcPathsBusiness != null) {
                        path = lfcPathsBusiness.parseRealDir(path, currentUserFolder);
                    }
                } catch (DataManagerException ex) {
                    // do nothing
                }
                inputs.put(name, path);

            } else {
                for (String v : values) {
                    try {
                        double value = Double.valueOf(v);

                        if (firstValue == -1) {
                            firstValue = value;

                        } else if (lastValue == -1) {

                            lastValue = value;
                            step = lastValue - firstValue;

                        } else {
                            if (step != (value - lastValue)) {
                                isList = true;
                                break;
                            } else {
                                step = value - lastValue;
                                lastValue = value;
                            }
                        }
                    } catch (NumberFormatException ex) {
                        isList = true;
                        break;
                    }
                }

                if (isList) {
                    StringBuilder sb = new StringBuilder();
                    for (String v : values) {
                        if (sb.length() > 0) {
                            sb.append("; ");
                        }
                        try {
                            if (lfcPathsBusiness != null) {
                                v = lfcPathsBusiness.parseRealDir(
                                    v, currentUserFolder);
                            }
                        } catch (DataManagerException ex) {
                            // do nothing
                        }
                        sb.append(v);
                    }
                    inputs.put(name, sb.toString());

                } else {
                    inputs.put(name, "Start: " + firstValue + " - Stop: "
                            + lastValue + " - Step: " + step);
                }
            }
        } else if (localName.equals("item")) {
            values.add(itemContent.toString().trim());
            itemContent = null;
            parsingItem = false;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (parsingItem) {
            itemContent.append(ch, start, length);
        }
    }
}
