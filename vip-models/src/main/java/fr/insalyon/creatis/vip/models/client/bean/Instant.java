package fr.insalyon.creatis.vip.models.client.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represent an instant used to represent movement.
 * @author forestier
 */
public class Instant implements Serializable, Comparable  {
	
	/** set of layer of the instant */
	private ArrayList<ObjectLayer> objectLayers;
	
	/** set of layer of the instant */
	private ArrayList<PhysicalParametersLayer> physicalParametersLayers;
	
	/** representation of the duration using a String (as XSDDuration isn't serializable) */
	private String duration;
	
	/** URI of the instance */
	private String URI;
	
	/**
	 * Create a new instant
	 */
	public Instant() {
		objectLayers = new ArrayList<ObjectLayer>();
		physicalParametersLayers = new ArrayList<PhysicalParametersLayer>();
	}
	
	/**
	 * Add the object layer to this instant
	 * @param layer an object layer
	 */
	public void addObjectLayer(ObjectLayer layer) {
		this.objectLayers.add(layer);
	}
	
	/**
	 * Add the physical parameter layer to this instant
	 * @param layer an object layer
	 */
	public void addPhysicalParametersLayer(PhysicalParametersLayer layer) {
		this.physicalParametersLayers.add(layer);
	}
	
	/**
	 * Remove the physical parameter layer layer of this instant
	 * @param index the index to remove
	 */
	public void removePhysicalParametersLayer(int index) {
		this.physicalParametersLayers.remove(index);
	}
	
	/**
	 * Remove the object layer of this instant
	 * @param index the index to remove
	 */
	public void removeObjectLayer(int index) {
		this.objectLayers.remove(index);
	}
	
	public ObjectLayer getObjectLayers(int index) {
		return objectLayers.get(index);
	}
	
	public ArrayList<ObjectLayer> getObjectLayers() {
		return objectLayers;
	}

	public void setObjectLayers(ArrayList<ObjectLayer> objectLayers) {
		this.objectLayers = objectLayers;
	}
	
	public ArrayList<PhysicalParametersLayer> getPhysicalParametersLayers() {
		return physicalParametersLayers;
	}
	
	public PhysicalParametersLayer getPhysicalParametersLayers(int index) {
		return physicalParametersLayers.get(index);
	}

	public void setPhysicalParametersLayers(
			ArrayList<PhysicalParametersLayer> physicalParametersLayers) {
		this.physicalParametersLayers = physicalParametersLayers;
	}
	
	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
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
		sb.append("[instant]"+duration+"\n");
		for (ObjectLayer objectLayer : this.objectLayers) {
			sb.append("\t[object layer] ("+objectLayer.getResolution()+") "+objectLayer.getType()+"\n");
			sb.append(objectLayer+"\n");
		}
		for (PhysicalParametersLayer physicalParametersLayer : this.physicalParametersLayers) {
			sb.append("\t[physical parameters layer] "+physicalParametersLayer.getType()+"\n");
			sb.append(physicalParametersLayer+"\n");
		}
		return sb.toString();
	}

	@Override
	public int compareTo(Object arg0) {
		Instant in = (Instant)arg0;
		double myDuration = Double.parseDouble(this.duration.substring(2, this.duration.length()-1));
		double hisDuration = Double.parseDouble(in.duration.substring(2, in.duration.length()-1));
		if(myDuration - hisDuration == 0) return 0;
		else if(myDuration - hisDuration > 0) return 1;
		else return -1;
	}
}
