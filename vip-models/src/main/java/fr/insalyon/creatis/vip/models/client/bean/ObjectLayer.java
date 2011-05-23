package fr.insalyon.creatis.vip.models.client.bean;

import java.io.Serializable;
import java.util.ArrayList;

import fr.insalyon.creatis.vip.models.client.bean.ObjectLayerPart.Format;
import fr.insalyon.creatis.vip.models.client.bean.PhysicalParametersLayer.PhysicalParameterType;
import fr.insalyon.creatis.vip.models.client.bean.SimulationObjectModel.ObjectType;

/**
 * Object layer gathering all the objets of a given type.
 * @author forestier
 */
public class ObjectLayer extends Layer implements Serializable  {
	
	/** Different possible types of resolution for the object layer */
	public enum Resolution {
		high,
		low,
		none,
	}
	
	/** Type of the layer */
	private ObjectType type;
	
	/** List of the parts of the layer */
	private ArrayList<ObjectLayerPart> layerParts;
	
	/** List of the physical parameters layers associated to this layer */
	private ArrayList<PhysicalParametersLayer> physicalParametersLayers;
	
	/** List of the physical parameters associated to this layer */
	private ArrayList<PhysicalParameter> physicalParameters;
	
	/** Resolution fo the layer */
	private Resolution resolution;
	
	/** URI of the instance */
	private String URI;
	
	/**
	 * Simple constructor
	 * @param layerType the type of the layer
	 */
	public ObjectLayer(ObjectType layerType) {
		this.layerParts = new ArrayList<ObjectLayerPart>();
		this.physicalParametersLayers = new ArrayList<PhysicalParametersLayer>();
		this.physicalParameters = new ArrayList<PhysicalParameter>();
		this.type = layerType;
	}
		
	public void addObjectLayerPart(ObjectLayerPart objectLayerPart) {
		objectLayerPart.setParent(this);
		this.layerParts.add(objectLayerPart);
	}
	
	public void removeObjectLayerPart(int index) {
		this.layerParts.remove(index);
	}
	
	public ArrayList<ObjectLayerPart> getLayerParts() {
		return layerParts;
	}
	
	public void setLayerParts(ArrayList<ObjectLayerPart> layerParts) {
		this.layerParts = layerParts;
	}
	
	public ObjectLayerPart getObjectLayerPart(int layerPartIndex) {
		return this.layerParts.get(layerPartIndex);
	}
	
	public void addPhysicalParametersLayer(PhysicalParametersLayer physicalParametersLayer) {
		this.physicalParametersLayers.add(physicalParametersLayer);
	}
	
	public void addPhysicalParameters(PhysicalParameter physicalParameter) {
		this.physicalParameters.add(physicalParameter);
	}
	
	public ObjectType getType() {
		return type;
	}

	public void setType(ObjectType type) {
		this.type = type;
	}

	public ArrayList<PhysicalParametersLayer> getPhysicalParametersLayers() {
		return physicalParametersLayers;
	}

	public void setPhysicalParametersLayers(
			ArrayList<PhysicalParametersLayer> physicalParametersLayers) {
		this.physicalParametersLayers = physicalParametersLayers;
	}
	
	public ArrayList<PhysicalParameter> getPhysicalParameters() {
		return physicalParameters;
	}

	public void setPhysicalParameters(ArrayList<PhysicalParameter> physicalParameters) {
		this.physicalParameters = physicalParameters;
	}

	public Resolution getResolution() {
		return resolution;
	}

	public void setResolution(Resolution resolution) {
		this.resolution = resolution;
	}
	
	/**
	 * Check if there is a layer of a physical parameters type defined for this object layer
	 * @param physicalParameterType the type of parameters
	 * @return true if there is a layer of this type, then false
	 */
	public boolean hasPhysicalParametersLayerOfType(PhysicalParameterType physicalParameterType) {
		for (PhysicalParametersLayer physicalParametersLayer : physicalParametersLayers) {
			if(physicalParametersLayer.getType().equals(physicalParameterType)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Return the layer of a physical parameters type defined for this object layer if it exits
	 * @param physicalParameterType the type of parameters
	 * @return the layer if it exist, then return null
	 */
	public PhysicalParametersLayer getPhysicalParametersLayerOfType(PhysicalParameterType physicalParameterType) {
		for (PhysicalParametersLayer physicalParametersLayer : physicalParametersLayers) {
			if(physicalParametersLayer.getType().equals(physicalParameterType)) {
				return physicalParametersLayer;
			}
		}
		return null;
	}
	
	/**
	 * Return the physical parameter of the asked type for this object layer (LUT) if it exits
	 * @param physicalParameterType the type of parameters
	 * @return the physical parameter if it exist, then return null
	 */
	public PhysicalParameter getPhysicalParametersOfType(PhysicalParameterType physicalParameterType) {
		for (PhysicalParameter physicalParameter : physicalParameters) {
			if(physicalParameter.getType().equals(physicalParameterType)) {
				return physicalParameter;
			}
		}
		return null;
	}
	
	/**
	 * Return the list of object part defined for this layer of the given format
	 * @param layerFormat the wanted format @see ObjectLayerPart
	 * @return the subset of object parts of the given format
	 */
	public ArrayList<ObjectLayerPart> getLayerParts(Format layerFormat) {
		ArrayList<ObjectLayerPart> myLayerParts = new ArrayList<ObjectLayerPart>();
		for (ObjectLayerPart objectLayerPart : layerParts) {
			if(objectLayerPart.getFormat() == layerFormat) {
				myLayerParts.add(objectLayerPart);
			}
		}
		return myLayerParts;
	}
	
	/**
	 * Return the list of file names used in the layer part of the object layer for the corresponding layerFormat
	 * @param layerFormat the format (mesh/voxel) 
	 * @return the list of distinct file names
	 */
	public ArrayList<String> getDistinctFilenames(Format layerFormat) {
		ArrayList<String> fileNames = new ArrayList<String>();
		
		ArrayList<ObjectLayerPart> objectLayerParts = getLayerParts(layerFormat);
		for (int i = 0; i < objectLayerParts.size(); i++) {
			ObjectLayerPart olp = objectLayerParts.get(i);
			for (String fileName : olp.getFileNames()) {
				if(!fileNames.contains(fileName))
					fileNames.add(fileName);
			}
		}
		
		return fileNames;
	}
	
	/**
	 * Return the look-up table as a two dimentional table of strings, [0] the name of the object, [1] the label of the object as string
	 * @return the look-up table
	 */
	public String[][] getLookUpTable() {
		ArrayList<ObjectLayerPart> objectLayerParts = getLayerParts(Format.voxel);
		String[][] lookupTable = new String[objectLayerParts.size()][2];
		
		for (int i = 0; i < objectLayerParts.size(); i++) {
			lookupTable[i][0] = objectLayerParts.get(i).getReferredObject().getObjectName();
			lookupTable[i][1] = objectLayerParts.get(i).getLabel()+"";
		}
		return lookupTable;
	}
	
	public String getURI() {
		return URI;
	}

	public void setURI(String uRI) {
		URI = uRI;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (ObjectLayerPart objectLayerPart : this.layerParts) {
			sb.append("\t\t"+objectLayerPart+"\n");
		}
		for (PhysicalParametersLayer physicalParametersLayer : this.physicalParametersLayers) {
			sb.append("\t\t"+physicalParametersLayer+" (layer)\n");
		}
		for (PhysicalParameter physicalParameter : this.physicalParameters) {
			sb.append("\t\t"+physicalParameter+" (LUT)\n");
		}
		return sb.toString();
	}
}
