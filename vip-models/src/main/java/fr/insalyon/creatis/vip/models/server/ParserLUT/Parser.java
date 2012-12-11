/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.models.server.ParserLUT;

import fr.insalyon.creatis.vip.models.client.ParserLUT.GenericProperty;
import fr.insalyon.creatis.vip.models.client.ParserLUT.GenericParameter;
import fr.insalyon.creatis.vip.models.client.ParserLUT.Distribution;
import fr.insalyon.creatis.vip.models.client.ParserLUT.PhysicalParameterLUT;
import fr.insalyon.creatis.vip.models.client.ParserLUT.Tissue;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author amarion
 */
public class Parser extends DefaultHandler {

    private PhysicalParameterLUT lut;
    private String currentTissueParam = null;
    private GenericProperty currentProperty = null;


    public Parser() {
        super();
    }

    public PhysicalParameterLUT getLut() {
        return lut;
    }
    
    
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        String cleanName = qName.replace("ns0:", "");

        if (cleanName.equals("paramLUT")) {
          
            lut = new PhysicalParameterLUT();
            lut.setName(attributes.getValue("name"));
        } else if (cleanName.equals("tissue")) {
            Tissue tissue = new Tissue();
            tissue.setName(attributes.getValue("name"));
            lut.getTissues().add(tissue);
        } else if (cleanName.equals("magneticProperties")) {
            GenericProperty gp =new GenericProperty();
            gp.setName("magneticProperties");
            lut.lastAdd().getProperties().add(gp);
        } else if (cleanName.equals("echogenicityProperties")) {
            GenericProperty gp =new GenericProperty();
            gp.setName("echogenicityProperties");
            lut.lastAdd().getProperties().add(gp);
            lut.lastAdd().lastAdd().setScatterersDensity(Double.parseDouble(attributes.getValue("scatterersDensity")));
        } else if (cleanName.equals("chemicalBlend")) {
            GenericProperty gp =new GenericProperty();
            gp.setName("chemicalBlend");
            lut.lastAdd().getProperties().add(gp);
            lut.lastAdd().lastAdd().setBlendDensity(Double.parseDouble(attributes.getValue("blendDensity")));
            lut.lastAdd().lastAdd().setBlendPhase(attributes.getValue("blendPhase"));
        } else if (cleanName.equals("protonDensity")) {
            GenericParameter gp =new GenericParameter();
            gp.setName("protonDensity");
            lut.lastAdd().lastAdd().getPhysParameters().add(gp);
        } else if (cleanName.equals("T1")) {
            GenericParameter gp =new GenericParameter();
            gp.setName("T1");
            lut.lastAdd().lastAdd().getPhysParameters().add(gp);
        } else if (cleanName.equals("T2")) {
            GenericParameter gp =new GenericParameter();
            gp.setName("T2");
            lut.lastAdd().lastAdd().getPhysParameters().add(gp);
        } else if (cleanName.equals("T2star")) {
            GenericParameter gp =new GenericParameter();
            gp.setName("T2star");
            lut.lastAdd().lastAdd().getPhysParameters().add(gp);
        } else if (cleanName.equals("susceptibility")) {
            GenericParameter gp =new GenericParameter();
            gp.setName("susceptibility");
            lut.lastAdd().lastAdd().getPhysParameters().add(gp);
        } else if (cleanName.equals("radiopharmaceuticalSpecificActivity")) {
            GenericParameter gp =new GenericParameter();
            gp.setName("radiopharmaceuticalSpecificActivity");
            lut.lastAdd().lastAdd().getPhysParameters().add(gp);
        } else if (cleanName.equals("radioactiveConcentration")) {
            GenericParameter gp =new GenericParameter();
            gp.setName("radioactiveConcentration");
            lut.lastAdd().lastAdd().getPhysParameters().add(gp);
        } else if (cleanName.equals("scatterersReflexivity")) {
            GenericParameter gp =new GenericParameter();
            gp.setName("scatterersReflexivity");
            lut.lastAdd().lastAdd().getPhysParameters().add(gp);
        } else if (cleanName.equals("scatterersDensity")) {
            GenericParameter gp =new GenericParameter();
            gp.setName("scatterersDensity");
            lut.lastAdd().lastAdd().getPhysParameters().add(gp);
        } else if (cleanName.equals("absoluteMassDensity")) {
            GenericParameter gp =new GenericParameter();
            gp.setName("absoluteMassDensity");
            lut.lastAdd().lastAdd().getPhysParameters().add(gp);
        } else if (cleanName.equals("relativeMassDensity")) {
            GenericParameter gp =new GenericParameter();
            gp.setName("relativeMassDensity");
            lut.lastAdd().lastAdd().getPhysParameters().add(gp);
        } else if (cleanName.equals("contrastAgentConcentration")) {
            GenericParameter gp =new GenericParameter();
            gp.setName("contrastAgentConcentration");
            lut.lastAdd().lastAdd().getPhysParameters().add(gp);
        }else if (cleanName.equals("radiopharmaceuticalConcentration")) {
            GenericParameter gp =new GenericParameter();
            gp.setName("radiopharmaceuticalConcentration");
            lut.lastAdd().lastAdd().getPhysParameters().add(gp);
        }else if (cleanName.equals("distribution")) {
            Distribution db = new Distribution();
            db.setName( attributes.getValue("name"));
            lut.lastAdd().lastAdd().lastAdd().setDistrib(db);
        } else if (cleanName.equals("distributionParam")) {
            lut.lastAdd().lastAdd().lastAdd().getDistrib().getParameters().put(attributes.getValue("name"), Double.parseDouble(attributes.getValue("value")));
        }else {
            System.err.println("Warning: ignored tag - \"" + cleanName + "\"");
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        String cleanName = qName.replace("ns0:", "");
        if (cleanName.equals("tissueParam")) {
            currentTissueParam = null;
        }
    }

    public PhysicalParameterLUT parse(File file) {
        try {
            System.out.println("Parsing file "+file);
            SAXParser parser = null;
            parser = SAXParserFactory.newInstance().newSAXParser();
            parser.parse(file, this);
            return lut;
        } catch (IOException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
