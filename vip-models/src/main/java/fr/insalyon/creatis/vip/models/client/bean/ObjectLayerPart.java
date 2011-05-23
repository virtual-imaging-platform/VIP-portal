package fr.insalyon.creatis.vip.models.client.bean;

import java.io.Serializable;
import java.util.ArrayList;

import fr.insalyon.creatis.vip.models.client.bean.SimulationObjectModel.ObjectType;

/**
 * Object layer part composing a layer and refering to an object.
 * @author forestier
 */
public class ObjectLayerPart implements Serializable, Comparable  {
	
	/** Different types of file format */
	public enum Format { 
		voxel,
		mesh
	};
	
	/** Parent layer */
	private Layer parent = null;
	
	/** Type of the layer-part */
	private SimulationObjectModel.ObjectType type;
	
	/** Format of the layer-part */
	private ObjectLayerPart.Format format;
	
	/** Label in the model (in the LUT) - only relevant for voxel object */
	private int label;
	
	/** Priority, only relevant for the mesh */
	private int priority;
	
	/** Names of the data file */
	private ArrayList<String> fileNames;
	
	/** URI of the instance */
	private String URI;
		
	/** Referred object */
	private SimulationObject referredObject;
	
	
	/**
	 * Create a new object layer part
	 * @param layerPartType the type of the layer part
	 * @param parentLayer its parent layer
	 */
	public ObjectLayerPart(ObjectType layerPartType, Layer parentLayer) {
		this.type = layerPartType;
		this.parent = parentLayer;
		this.fileNames = new ArrayList<String>();
	}
	
	void setReferredObjet(SimulationObject objectModel) {
		this.referredObject = objectModel;
	}

	public Layer getParent() {
		return parent;
	}

	public void setParent(Layer parent) {
		this.parent = parent;
	}

	public SimulationObjectModel.ObjectType getType() {
		return type;
	}

	public void setType(SimulationObjectModel.ObjectType type) {
		this.type = type;
	}

	public ObjectLayerPart.Format getFormat() {
		return format;
	}

	public void setFormat(ObjectLayerPart.Format format) {
		this.format = format;
	}
	
	public void addFileName(String fileName) {
		this.fileNames.add(fileName);
	}

	public ArrayList<String> getFileNames() {
		return fileNames;
	}

	public void setFileNames(ArrayList<String> fileNames) {
		this.fileNames = fileNames;
	}
	
	public int getLabel() {
		return label;
	}

	public void setLabel(int label) {
		this.label = label;
	}
	
	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getURI() {
		return URI;
	}

	public void setURI(String uRI) {
		URI = uRI;
	}

	public SimulationObject getReferredObject() {
		return referredObject;
	}

	public void setReferredObject(SimulationObject referredObject) {
		this.referredObject = referredObject;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[Type:"+this.type +"] [Format:"+this.format+"] [Object:"+referredObject.getObjectName()+"] Files["+this.fileNames+"] [label:"+this.label+"] ");
		return sb.toString();
	}

	@Override
	public int compareTo(Object arg0) {
		ObjectLayerPart olp = (ObjectLayerPart)arg0;
		if(this.format == Format.mesh && olp.format == Format.mesh) {
			return this.priority - olp.priority;
		}
		if(this.format != olp.format || (this.format == Format.voxel && olp.format == Format.voxel)) {
			return this.label - olp.label;
		}
		return 0;
	}
}
