package fr.insalyon.creatis.vip.models.client.bean;

import java.io.Serializable;
import java.util.ArrayList;

import fr.insalyon.creatis.vip.models.client.bean.PhysicalParametersLayer.PhysicalParameterType;

/**
 * Represent a physical parameters.
 * @author forestier
 *
 */
public class PhysicalParameter implements Serializable {
	
	/** Paramter type */
	private PhysicalParameterType type;
	
	/** File names, used to file names of LUT */
	private ArrayList<String> fileNames;
	
	/** URI of the instance */
	private String URI;
	
	public PhysicalParameter(PhysicalParameterType type, ArrayList<String> fileNames) {
		this.type = type;
		this.fileNames = fileNames;
	}

	public PhysicalParameterType getType() {
		return type;
	}

	public void setType(PhysicalParameterType type) {
		this.type = type;
	}

	public ArrayList<String> getFileNames() {
		return fileNames;
	}

	public void setFileNames(ArrayList<String> fileNames) {
		this.fileNames = fileNames;
	}

	public String getURI() {
		return URI;
	}

	public void setURI(String uRI) {
		URI = uRI;
	}
	
	@Override
	public String toString() {
		return "\t\t"+this.type +" "+this.fileNames;
	}
}
