package fr.insalyon.creatis.vip.models.client.bean;

/**
 * Simple class used to query the models which stores basic information on the model (types, name, etc.)
 * @author forestier
 */
public class SimulationObjectModelLight {
	
	/** Model name */
	private String modelName;
	
	/** Used to know if the model is pathological, anatomical, etc. */
	private boolean[] semanticAxes;
	
	/** Is the model longitudinal follow up (#timepoint > 1) */
	private boolean isLongitudial;
	
	/** Is the model moving (#instant > 1 @ 1 timepoint) */
	private boolean isMoving;
	
	// TODO handle model author
	
	/** URI of the instance */
	private String URI;
	
	public SimulationObjectModelLight() {
		semanticAxes = new boolean[5];
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public boolean[] getSemanticAxes() {
		return semanticAxes;
	}

	public void setSemanticAxes(boolean[] semanticAxes) {
		this.semanticAxes = semanticAxes;
	}
	
	public void setSemanticAxesValue(boolean value, int index) {
		this.semanticAxes[index] = value;
	}
	
	public void setLongitudinal(boolean isLongitudial) {
		this.isLongitudial = isLongitudial;
	}
	
	public void setMoving(boolean isMoving) {
		this.isMoving = isMoving;
	}
	
	public boolean isMoving() {
		return isMoving;
	}
	
	public boolean isLongitudinal() {
		return isLongitudial;
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
		sb.append("[");
		for (int i = 0; i < semanticAxes.length; i++) {
			sb.append(semanticAxes[i]+"; ");
		}
		sb.append("]");
		return modelName + sb;
	}
}
