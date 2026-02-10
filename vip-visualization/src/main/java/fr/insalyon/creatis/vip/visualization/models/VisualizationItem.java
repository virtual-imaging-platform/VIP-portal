package fr.insalyon.creatis.vip.visualization.models;

import com.google.gwt.user.client.rpc.IsSerializable;

/** @author Tristan Glatard */
public class VisualizationItem implements IsSerializable{
    private String lfn;

    // Extension of raw file if url is a mhd file.  Empty string otherwise.
    private String extension;

    public VisualizationItem() {}

    public VisualizationItem(String lfn, String extension) {
        this.lfn = lfn;
        this.extension = extension;
    }

    public String getLfn() {
        return lfn;
    }

    public void setLfn(String lfn) {
        this.lfn = lfn;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
