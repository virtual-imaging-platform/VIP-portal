package fr.insalyon.creatis.vip.models.client.bean;

import java.io.Serializable;

import fr.insalyon.creatis.vip.models.client.bean.SimulationObjectModel.ObjectType;

/**
 * Represent a simulation object of one type.
 * @author forestier
 */
public class SimulationObject implements Serializable  {
	
	/** Internal name of the object */
	private String objectName;
	
	/** Type of the layer-part */
	private SimulationObjectModel.ObjectType type;
		
	/** URI of the instance */
	private String URI;
	
	public SimulationObject(ObjectType objectType, String objectName) {
		this.type = objectType;
		this.objectName = objectName;
	}
	
	public ObjectType getType() {
		return type;
	}

	public void setType(SimulationObjectModel.ObjectType type) {
		this.type = type;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}
	
	public String getURI() {
		return URI;
	}

	public void setURI(String uRI) {
		URI = uRI;
	}
}
