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
package fr.insalyon.creatis.vip.models.client.view;

import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import fr.insalyon.creatis.vip.models.client.ModelConstants;

/**
 *
 * @author cervenansky
 */
public class ModelToolStrip  extends ToolStrip{
    private ToolStripButton tsMRI;
    private ToolStripButton tsPET;
    private ToolStripButton tsCT;
    private ToolStripButton tsUS;
    
    ModelToolStrip()
    {
        super();
        setWidth100();
    }
    
    public void initCheck()
    {
        tsMRI   = new ToolStripButton("MRI");
        isKo("MRI");
        addMember(tsMRI);
        
        tsPET   = new ToolStripButton("PET");
        isKo("PET");
        addMember(tsPET);
        
        tsCT   = new ToolStripButton("CT");
        isKo("CT");
        addMember(tsCT);
        
        tsUS   = new ToolStripButton("UltraSound");
        isKo("UltraSound");
        addMember(tsUS);
        addSeparator();
    }

    public void isOk(String caption)
    {
          ToolStripButton ok = null;
       if (caption.equals("MRI"))
                ok = tsMRI;
       else if (caption.equals("PET"))
                ok = tsPET;
       else if (caption.equals("CT"))
                ok = tsCT;
        else if (caption.equals("UltraSound"))
                ok = tsUS;
       ok.setIcon(ModelConstants.APP_IMG_OK);
       ok.setTooltip("This model can be used in a simulation of this modality.");
    }
    
    public void isKo(String caption)
    {
          ToolStripButton ok = null;
       if (caption.equals("MRI"))
                ok = tsMRI;
       else if (caption.equals("PET"))
                ok = tsPET;
       else if (caption.equals("CT"))
                ok = tsCT;
        else if (caption.equals("UltraSound"))
                ok = tsUS;
        ok.setTooltip("This model lacks physical parameters to be used in a simulation of this modality.");
        ok.setIcon(ModelConstants.APP_IMG_KO);
    }

    
    private ToolStripButton findButton(String caption)
    {
       ToolStripButton ok = null;
       if (caption.equals("MRI"))
                ok = tsMRI;
       else if (caption.equals("PET"))
                ok = tsPET;
       else if (caption.equals("CT"))
                ok = tsCT;
        else if (caption.equals("Ultrasound"))
                ok = tsUS;
        return ok;
    }
    
}
