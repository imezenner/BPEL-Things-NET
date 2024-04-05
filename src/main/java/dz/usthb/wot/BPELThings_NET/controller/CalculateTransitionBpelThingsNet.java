package dz.usthb.wot.BPELThings_NET.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;

import dz.usthb.wot.BPELThings_NET.model.BpelThingsNetComponent;
import dz.usthb.wot.BPELThings_NET.model.Place;
import dz.usthb.wot.BPELThings_NET.model.TransitionType;

public class CalculateTransitionBpelThingsNet {
	
	public static void calculationTransitionBpelThingsNet (ArrayList<BpelThingsNetComponent> bpelThingsNetList)
    {
    	for (int i = 0; i < bpelThingsNetList.size(); i++)
        {
    		BpelThingsNetComponent bpelThingsNetComponent = bpelThingsNetList.get(i);
    		if ((bpelThingsNetComponent.getT1().getType() == TransitionType.Simple) && (bpelThingsNetComponent.getT1().getFd() == null))
    		{
    			ArrayList<Place> placeList;
    			int idTransition = bpelThingsNetList.get(i).getT1().getId();
    			if ((placeList = getInputPlaceOfTransition(bpelThingsNetList, idTransition)).size() > 0)
    				bpelThingsNetList.get(i).getT1().setFd(maxEdOfPlaceList(placeList));
    			else
    				if ((placeList = getOutputPlaceOfTransition(bpelThingsNetList, idTransition)).size() > 0)
    					bpelThingsNetList.get(i).getT1().setFd(minSdOfPlaceList(placeList));
    		}
    		
    		if ((bpelThingsNetComponent.getT2().getType() == TransitionType.Simple) && (bpelThingsNetComponent.getT2().getFd() == null))
    		{
    			ArrayList<Place> placeList;
    			int idTransition = bpelThingsNetList.get(i).getT2().getId();
    			if ((placeList = getInputPlaceOfTransition(bpelThingsNetList, idTransition)).size() > 0)
    				bpelThingsNetList.get(i).getT2().setFd(maxEdOfPlaceList(placeList));
    		}	
        }
    }
	
	private static LocalDateTime maxEdOfPlaceList (ArrayList<Place> placeList)
    {
    	return placeList.stream()
    			.filter(p -> p.getEd() != null)
    			.map(Place::getEd)
    			.max(LocalDateTime::compareTo).get();    	
    }
	
	private static LocalDateTime minSdOfPlaceList (ArrayList<Place> placeList)
    {
    	return placeList.stream()
    			.filter(p -> p.getSd() != null)
    			.map(Place::getSd)
    			.max(LocalDateTime::compareTo).get();    	
    }
    
    private static ArrayList<Place> getInputPlaceOfTransition (ArrayList<BpelThingsNetComponent> bpelThingsNetList, 
    		int idTransition)
    {
    	ArrayList<Place> placeList = new ArrayList<Place>();
    	
    	for (int i = 0; i < bpelThingsNetList.size(); i++)
    		if (bpelThingsNetList.get(i).getT2().getId() == idTransition)
    			placeList.add(bpelThingsNetList.get(i).getP());
    	
    	return placeList;
    }
    
    private static ArrayList<Place> getOutputPlaceOfTransition (ArrayList<BpelThingsNetComponent> bpelThingsNetList, 
    		int idTransition)
    {
    	ArrayList<Place> placeList = new ArrayList<Place>();
    	
    	for (int i = 0; i < bpelThingsNetList.size(); i++)
    		if (bpelThingsNetList.get(i).getT1().getId() == idTransition)
    			placeList.add(bpelThingsNetList.get(i).getP());
    	
    	return placeList;
    }

}
