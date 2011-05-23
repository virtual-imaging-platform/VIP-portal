package fr.insalyon.creatis.vip.models.client.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represent a simulation object model.
 * @author forestier
 */
public class SimulationObjectModel implements Serializable {
	
	/** Different possible types for object/part/layer */
	public enum ObjectType { 
		anatomical,
		pathological, 
		geometrical,
		foreign_body,
		external_agent 
	};
	
	/** Model name given by the model author */
	private String modelName;

	/** List of the timepoints composing the model */
	private ArrayList<Timepoint> timepoints;
	
	/** URI of the instance */
	private String URI;
	
	public SimulationObjectModel() {
		timepoints = new ArrayList<Timepoint>();
	}
	
	public void addTimepoint(Timepoint tp) {
		this.timepoints.add(tp);
	}
	
	public Timepoint getTimepoint(int timePointIndex) {
		return this.timepoints.get(timePointIndex);
	}
	
	public Instant getInstant(int timePointIndex, int instantIndex) {
		return getTimepoint(timePointIndex).getInstant(instantIndex);
	}
	
	public void addObjectLayer(int timePointIndex, int instantIndex, ObjectLayer objectLayer) {
		this.timepoints.get(timePointIndex).getInstant(instantIndex).addObjectLayer(objectLayer);
	}
	
	public ArrayList<Timepoint> getTimepoints() {
		return timepoints;
	}

	public void setTimepoints(ArrayList<Timepoint> timepoints) {
		this.timepoints = timepoints;
	}
	
	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
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
		for (Timepoint timePoint : this.timepoints) {
			sb.append(timePoint+"\n");
		}
		return sb.toString();
	}
}
