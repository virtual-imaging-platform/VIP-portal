package fr.insalyon.creatis.vip.models.client.bean;

import java.util.ArrayList;

import fr.insalyon.creatis.vip.models.client.bean.ObjectLayer.Resolution;
import fr.insalyon.creatis.vip.models.client.bean.PhysicalParametersLayer.PhysicalParameterType;
import fr.insalyon.creatis.vip.models.client.bean.SimulationObjectModel.ObjectType;

/**
 * Util class to retrieve information on the models.
 * @author forestier
 */
public class SimulationObjectModelUtil {
	
	/**
	 * Check the presence of an objectlayer of a type at a given timepoint/instant
	 * @param objectModel the model
	 * @param timePointIndex the timepoint index
	 * @param instantIndex the instant index
	 * @param objectType the type of layer to search for
	 * @param layerResolution the resolution of the layer to search for
	 * @return the found objectlayer, equals null if no layer of this type is found at the given instant
	 */
	public static ObjectLayer getObjectLayerOfTypeOfResolution(SimulationObjectModel objectModel, int timePointIndex, int instantIndex, ObjectType objectType, Resolution layerResolution) {
		Instant instant = objectModel.getTimepoint(timePointIndex).getInstant(instantIndex);
		ArrayList<ObjectLayer> objectLayers = instant.getObjectLayers();
		ObjectLayer foundLayer = null;
		for (ObjectLayer objectLayer : objectLayers) {
			if(objectLayer.getType() == objectType && objectLayer.getResolution() == layerResolution) {
				foundLayer = objectLayer;
				break;
			}
		}
		return foundLayer;
	}
	
	/**
	 * Check the presence of resolution types for a given layer type
	 * @param objectModel the model
	 * @param timePointIndex the timepoint index
	 * @param instantIndex the instant index
	 * @param objectType the type of the layer
	 * @return return "null" is there is no layer of the asked type, "none" if there is one layer, "high" if there are two layers (high/low)
	 */
	public static Resolution getResolutionOfLayerOfType(SimulationObjectModel objectModel, int timePointIndex, int instantIndex, ObjectType objectType) {
		ObjectLayer objectLayer = getObjectLayerOfTypeOfResolution(objectModel, timePointIndex, instantIndex, objectType, ObjectLayer.Resolution.none);
		if(objectLayer == null) {
			objectLayer = getObjectLayerOfTypeOfResolution(objectModel, timePointIndex, instantIndex, objectType, ObjectLayer.Resolution.high);
			if(objectLayer == null) {
				return null;
			} else {
				ObjectLayer objectLayer2 = getObjectLayerOfTypeOfResolution(objectModel, timePointIndex, instantIndex, objectType, ObjectLayer.Resolution.low);
				return ObjectLayer.Resolution.high;
			}
		} else {
			return ObjectLayer.Resolution.none;
		}
	}
	
	
	/**
	 * Check if at each instant for each type of physical parameters, there is only one layer of this type not linked to an object layer
	 * @param simulationObjectModel
	 * @return
	 */
	public static boolean checkOnlyOneTypeOfPhysicalParameters(SimulationObjectModel simulationObjectModel) {
		boolean isOK = true;
		
		for (int i = 0; i < simulationObjectModel.getTimepoints().size(); i++) {
			for (int j = 0; j < simulationObjectModel.getTimepoint(i).getInstants().size(); j++) {
				Instant instant = simulationObjectModel.getTimepoint(i).getInstant(j);
				boolean[] check = new boolean[PhysicalParameterType.values().length];
				for (int k = 0; k < check.length; k++) {
					check[k] = false;
				}
				for (int k = 0; k < instant.getPhysicalParametersLayers().size(); k++) {
					PhysicalParametersLayer physicalParametersLayer = instant.getPhysicalParametersLayers(k);
					if(!check[physicalParametersLayer.getType().ordinal()]) {
						check[physicalParametersLayer.getType().ordinal()] = true;
					} else {
						return false;
					}
				}
			}
		}
		
		return isOK;
	}
}
