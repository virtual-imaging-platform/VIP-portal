package fr.insalyon.creatis.vip.models.client.bean;

import fr.insalyon.creatis.vip.models.client.bean.SimulationObjectModel.ObjectType;

/**
 * Class used when searching for objets in the ontology and containing the score from string comparison.
 * @see Apache Lucene
 * @author forestier
 */
public class SimulationObjectMatch extends SimulationObject {
	
	/** Matching score of the object */
	private float score;
	
	public SimulationObjectMatch(String name, ObjectType type, float score) {
		super(type, name);
		this.score = score;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}
	
	@Override
	public String toString() {
		return this.getObjectName()+"\n"+this.getType()+"\n"+this.score;
	}
}
