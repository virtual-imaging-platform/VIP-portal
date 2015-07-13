/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.creatis.insa-lyon.fr/~silva
 *
 * This software is a grid-enabled data-driven workflow manager and editor.
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
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
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.application.server.business.simulation.parser;

import fr.insalyon.creatis.vip.application.client.bean.Source;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Rafael Silva
 */
public class GwendiaParser extends AbstractWorkflowParser {

    private Source currentSource = null;
    private String text = null;
    
    
    public GwendiaParser() {
        super();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        if (localName.equals("source")) {
            Source s = new Source(attributes.getValue("name"),attributes.getValue("user-level"),attributes.getValue("description"));
            sources.add(s);
            currentSource = s;
            return;
        }
        if(localName.equals("source-comment") || localName.equals("description"))
            text="";
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
         if(text != null) {
            String chars = new String(ch);
            text += chars.substring(start,start+length);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(localName.equals("source")){
            currentSource = null;
        }
        if (localName.equals("source-comment")) {
            currentSource.setDescription(text);
        }
        if (localName.equals("description")) {
            description = text;
        }
    }
    
}
