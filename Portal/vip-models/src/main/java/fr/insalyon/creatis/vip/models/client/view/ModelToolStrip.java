/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
