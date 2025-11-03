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

import fr.insalyon.creatis.vip.core.client.bean.Triplet;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fr.insalyon.creatis.vip.datamanager.server.business.LfcPathsBusiness;

import org.apache.commons.io.FilenameUtils;
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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

/**
 * Parse a m2 input file.
 *
 * This stores data in fields and this is not threadsafe. So it cannot be used
 * as a spring singleton and this needs prototype scope.
 *
 */
@Service
@Scope("prototype")
public class InputFileParser {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private String currentUserFolder;
    private LfcPathsBusiness lfcPathsBusiness;
    private Map<String, String> inputs = new HashMap<>();

    @Autowired
    public final void setLfcPathsBusiness(LfcPathsBusiness lfcPathsBusiness) {
        this.lfcPathsBusiness = lfcPathsBusiness;
    }

    public InputFileParser() {
        this(null);
    }

    public InputFileParser(String currentUserFolder) {
        this.currentUserFolder = currentUserFolder;
    }

    // If the filePath do not contain an extension, then JSON and XML will be tested
    // the first founded file will be used
    public Map<String, String> parse(Path path) throws BusinessException {
        String ext = FilenameUtils.getExtension(path.toString());

        if (ext.isEmpty()) {
            for (String candidateExt : List.of("json", "xml")) {
                Path candidate = path.resolveSibling(path.getFileName() + "." + candidateExt);
                if (candidate.toFile().exists()) {
                    path = candidate;
                    ext = candidateExt;
                    break;
                }
            }
        }

        switch (ext) {
            case "xml":
                return handleXML(path.toFile());
            case "json":
                return handleJSON(path.toFile());
            default:
                throw new BusinessException("Cannot find inputs file at this location: " + path);
        }
    }

    public Map<String, String> handleXML(File file) throws BusinessException {
        try {
            XMLHandler handler = new XMLHandler();

            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            parserFactory.setNamespaceAware(true);

            XMLReader reader = parserFactory.newSAXParser().getXMLReader();
            reader.setContentHandler(handler);
            reader.parse(new InputSource(new FileReader(file)));

            return inputs;
        } catch (IOException | SAXException | ParserConfigurationException e) {
            logger.error("Error parsing {}", file.getName(), e);
            throw new BusinessException(e);
        }
    }

    public Map<String, String> handleJSON(File file) throws BusinessException {
        ObjectMapper mapper = new ObjectMapper();

        try {
            Map<String, List<String>> data = mapper.readValue(file, new TypeReference<Map<String, List<String>>>() {
            });

            data.forEach((name, items) -> {
                if (items.size() == 1) {
                    String val = items.get(0);
                    inputs.put(name, pathHandler(val));
                } else {
                    handleList(name, items);
                }
            });
        } catch (IOException e) {
            logger.error("Error parsing {}", file.getName(), e);
            throw new BusinessException(e);
        }
        return inputs;
    }

    private String pathHandler(String path) {
        try {
            if (lfcPathsBusiness != null) {
                path = lfcPathsBusiness.parseRealDir(path, currentUserFolder);
            }
        } catch (DataManagerException ex) {
            // do nothing
        }
        return path;
    }

    private Triplet<Double, Double, Double> getStartStopStep(List<String> values) {
        double step = -1;
        double start = -1;
        double stop = -1;

        for (String v : values) {
            try {
                double value = Double.valueOf(v);

                if (start == -1) {
                    start = value;

                } else if (stop == -1) {
                    stop = value;
                    step = stop - start;

                } else {
                    if (step != (value - stop)) {
                        return null;
                    } else {
                        stop = value;
                    }
                }
            } catch (NumberFormatException ex) {
                return null;
            }
        }
        return new Triplet<Double, Double, Double>(start, stop, step);
    }

    private String formatListString(List<String> items) {
        return items.stream()
                .map(this::pathHandler)
                .collect(Collectors.joining("; "));
    }

    private void handleList(String name, List<String> items) {
        Triplet<Double, Double, Double> startStopStep = getStartStopStep(items);
        if (startStopStep == null) {
            inputs.put(name, formatListString(items));
        } else {
            inputs.put(name, "Start: " + startStopStep.getFirst() + " - Stop: "
                    + startStopStep.getSecond() + " - Step: " + startStopStep.getThird());
        }
    }

    class XMLHandler extends DefaultHandler {
        private boolean parsingItem = false;
        private List<String> values;
        private String name;
        private StringBuilder itemContent;

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
            if (localName.equals("source")) {
                if (values.size() == 1) {
                    String path = values.get(0);
                    inputs.put(name, pathHandler(path));

                } else {
                    handleList(name, values);
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
}
