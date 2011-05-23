package fr.insalyon.creatis.vip.models.client.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class PhysicalParametersLayer implements Serializable  {
	
	/** Type of physical parameters of the layer */
	public enum PhysicalParameterType { 
		T1,
		T2, 
		T2s, 
		susceptibility,
		protonDensity,
		radioactiviy,
		chemicalBlend,
		scatterers,
	};
	
	/** Type of the layer */
	PhysicalParametersLayer.PhysicalParameterType type;

	/** Names of the data file */
	private ArrayList<String> fileNames;
	
	/** URI of the instance */
	private String URI;
	
	/** b0 */
	private double B0;
	
	public PhysicalParametersLayer(PhysicalParametersLayer.PhysicalParameterType layerType) {
		this.type = layerType;
		this.fileNames = new ArrayList<String>();
	}
	
	public PhysicalParametersLayer.PhysicalParameterType getType() {
		return type;
	}

	public void setType(PhysicalParametersLayer.PhysicalParameterType type) {
		this.type = type;
	}
	
	public void addFileName(String fileName) {
		this.fileNames.add(fileName);
	}

	public ArrayList<String> getFileName() {
		return fileNames;
	}

	public void setFileName(ArrayList<String> fileNames) {
		this.fileNames = fileNames;
	}
	
	public double getB0() {
		return B0;
	}

	public void setB0(double b0) {
		B0 = b0;
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
		sb.append("\t\t"+this.type +" "+this.fileNames+" "+(B0 != -1 ? " b0: "+ B0 : ""));
		return sb.toString();
	}
}
