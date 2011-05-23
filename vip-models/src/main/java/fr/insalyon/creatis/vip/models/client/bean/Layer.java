package fr.insalyon.creatis.vip.models.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Abstract layer.
 * @author forestier
 */
public class Layer implements Serializable, IsSerializable{
	
	/** URI of the instance */
	private String URI;

	public String getURI() {
		return URI;
	}

	public void setURI(String uRI) {
		URI = uRI;
	}
}
