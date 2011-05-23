package fr.insalyon.creatis.vip.models.client.bean;

import java.io.Serializable;
import java.util.ArrayList;
//import java.util.Calendar;

/**
 * Represent an time point used to represent different state of the model.
 * @author forestier
 */
public class Timepoint implements Serializable, Comparable  {
	
	/** List of instants of the timepoint */
	private ArrayList<Instant> instants = null;
	
	/** Starting date */
//	private Calendar startingDate;
	
	/** URI of the instance */
	private String URI;
	
	public Timepoint() {
		instants = new ArrayList<Instant>();
	}
	
	public Instant getInstant(int instantIndex) {
		return this.instants.get(instantIndex);
	}
	
	public void addInstant(Instant i) {
		this.instants.add(i);
	}
		
	public ArrayList<Instant> getInstants() {
		return instants;
	}

	public void setInstants(ArrayList<Instant> instants) {
		this.instants = instants;
	}
	
	//public Calendar getStartingDate() {
	//	return startingDate;
	//}

	//public void setStartingDate(Calendar startingDate) {
	//	this.startingDate = startingDate;
	//}

	public String getURI() {
		return URI;
	}

	public void setURI(String uRI) {
		URI = uRI;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
	//	sb.append("[timepoint] "+startingDate.getTime()+"\n");
		for (Instant instant : this.instants) {
			sb.append(instant+"\n");
		}
		return sb.toString();
	}

	@Override
	public int compareTo(Object arg0) {
		Timepoint tp = (Timepoint)arg0;
                return 1;
	//	return this.startingDate.compareTo(tp.startingDate);
	}
}
