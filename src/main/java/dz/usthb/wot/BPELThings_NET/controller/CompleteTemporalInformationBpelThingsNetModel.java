package dz.usthb.wot.BPELThings_NET.controller;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import dz.usthb.wot.BPELThings_NET.model.BpelThingsNetComponent;
import dz.usthb.wot.BPELThings_NET.model.Place;

public class CompleteTemporalInformationBpelThingsNetModel {
	
	public static void calculationTemporalInformationBpelThingsNetModel (ArrayList<BpelThingsNetComponent> bpelThingsNetList)
    {
    	int i;
    	ArrayList<Place> placeList;
    	for (i = 0; i < bpelThingsNetList.size(); i++)
    	{
    		Place p = bpelThingsNetList.get(i).getP();
    		localCalculationPlace(p);
    		
    		if ((p.getName() == "R") || (p.getName() == "SEQ"))
    		{
    			placeList = getChildPlaceOfSequence (bpelThingsNetList, 
    					bpelThingsNetList.get(i).getReferenceToChildPlace().getIdFirstTransition(),
    					bpelThingsNetList.get(i).getReferenceToChildPlace().getIdLastTransition());
    			
    			calculationBetweenSiblingPlacesOfSequence (placeList);
    			
    			calculationBetweenSequenceParentAndItsChildern(p, placeList);
    		}
    		else
    			if ((p.getName() == "PAR") || (p.getName() == "IF"))
        		{
        			placeList = getChildPlaceOfFlow (bpelThingsNetList, 
        					bpelThingsNetList.get(i).getReferenceToChildPlace().getIdFirstTransition(),
        					bpelThingsNetList.get(i).getReferenceToChildPlace().getIdLastTransition());
        			
        			calculationBetweenSiblingPlacesOfParallel(placeList);
        			
        			calculationBetweenParallelParentAndItsChildern(p, placeList);
        		}
    			else
    				if ((p.getName() == "WHILE") || (p.getName() == "REPEATUNTIL"))
    	    		{
    	    			placeList = getChildPlaceOfSequence (bpelThingsNetList, 
    	    					bpelThingsNetList.get(i).getReferenceToChildPlace().getIdFirstTransition(),
    	    					bpelThingsNetList.get(i).getReferenceToChildPlace().getIdLastTransition());
    	    			
    	    			calculationBetweenIterativeParentAndItsChildern(p, placeList);
    	    		}
    	}
    	
    	for (i = 0; i < bpelThingsNetList.size(); i++)
    	{
    		Place p = bpelThingsNetList.get(i).getP();
    		if ((p.getSd() == null) || (p.getEd() == null) || (p.getDur() == null))
    			break;
    	}
    	if (i != bpelThingsNetList.size())
    		calculationTemporalInformationBpelThingsNetModel(bpelThingsNetList);
    }
	
	private static void localCalculationPlace (Place place)
    {
    	// S(P) = E(P) - D(P)
    	if ((place.getSd() == null) && (place.getEd() != null) && (place.getDur() != null))
    		place.setSd(place.getEd().minusSeconds(place.getDur()));

    	// E(P) = S(P) + D(P)
		if ((place.getEd() == null) && (place.getSd() != null) && (place.getDur() != null))
			place.setEd(place.getSd().plusSeconds(place.getDur()));

		// D(P) = E(P) - S(P)
		if ((place.getDur() == null) && (place.getSd() != null) && (place.getEd() != null))
		{
			long diffInSeconds = ChronoUnit.SECONDS.between(place.getSd(), place.getEd());
			place.setDur(Long.valueOf(diffInSeconds));
		}
		
		/*
		System.out.println(place.getSd());
		System.out.println(place.getEd());
		System.out.println(place.getDur());
		*/
    }
	
	private static void calculationBetweenSiblingPlacesOfSequence(ArrayList<Place> siblingPlaces)
    {
    	// E(Pi) = S(Pi+1), where  i ∈ [1, n-1]
    	// S(Pi) = E(Pi-1), where  i ∈ [2, n] 
    	for (int i = 0; i < siblingPlaces.size() - 1; i++)
    	{
    		Place place1 = siblingPlaces.get(i);
    		Place place2 = siblingPlaces.get(i+1);
    		
    		if ((place1.getEd() == null) && (place2.getSd() != null))
    			place1.setEd(place2.getSd());
    		
    		if ((place2.getSd() == null) && (place1.getEd() != null))
    			place2.setSd(place1.getEd());
    	}
    }
	
	private static void calculationBetweenSiblingPlacesOfParallel(ArrayList<Place> siblingPlaces)
    {
    	// S(Pi) = S(Pi+1), where  i = 1
    	// S(Pi) = S(Pi-1) = S(Pi+1), where  i ∈ [2, n-1]
    	// S(Pi) = S(Pi-1), where  i = n
    	LocalDateTime sdPlace1, sdPlace2, sdPlace3;
    	Place place;
    	for (int i = 0; i < siblingPlaces.size(); i++)
    	{
    		place = siblingPlaces.get(i);
    		
    		sdPlace1 = siblingPlaces.get(i).getSd();
    		
    		sdPlace2 = i < siblingPlaces.size() - 1 ? siblingPlaces.get(i+1).getSd() : null;
    		
    		sdPlace3 = i > 0 ? siblingPlaces.get(i-1).getSd() : null;
    		
    		if (sdPlace1 == null)
    		{
    			if(sdPlace2 != null)
    				place.setSd(sdPlace2);
    			if(sdPlace3 != null)
    				place.setSd(sdPlace3);
    		}
    	}
    }

	private static void calculationBetweenSequenceParentAndItsChildern(Place parentPlace,
    		ArrayList<Place> childrenPlaces)
    {
    	Place P1 = childrenPlaces.get(0);
    	Place Pn = childrenPlaces.get(childrenPlaces.size()-1);
    
    	// S(CP) = S(P1)
    	if((parentPlace.getSd() == null) && (P1.getSd() != null)) 
    		parentPlace.setSd(P1.getSd());
    	if((P1.getSd() == null) && (parentPlace.getSd() != null)) 
        	P1.setSd(parentPlace.getSd());
    	
    	// E(CP) = S(Pn)
    	if((parentPlace.getEd() == null) && (Pn.getEd() != null)) 
    		parentPlace.setEd(Pn.getEd());
    	if((Pn.getEd() == null) && (parentPlace.getEd() != null)) 
        	Pn.setEd(parentPlace.getEd());
    	
    	// D(CP) = ∑ D(Pi), where  i ∈ [1, n]
    	long duration = 0;
    	int i;
    	for(i=0; i < childrenPlaces.size(); i++)
    	{
    		if(childrenPlaces.get(i).getDur() == null)
    			break;
    		
    		duration += childrenPlaces.get(i).getDur();
    	}
    	if (i == childrenPlaces.size())
    		parentPlace.setDur(Long.valueOf(duration));
    }

	private static void calculationBetweenParallelParentAndItsChildern(Place parentPlace,
    		ArrayList<Place> childrenPlaces)
    {
    	// S(CP) = S(Pi), where i ∈ [1, n]
    	if(parentPlace.getSd() == null)
	    	for(int i=0; i < childrenPlaces.size(); i++)
	    	{
	    		if(childrenPlaces.get(i).getSd() != null)
	    		{
	    			parentPlace.setSd(childrenPlaces.get(i).getSd());
	    			break;
	    		}
	    	}
    	
    	if(parentPlace.getSd() != null)
	    	for(int i=0; i < childrenPlaces.size(); i++)
	    	{
	    		if(childrenPlaces.get(i).getSd() == null)
	    			childrenPlaces.get(i).setSd(parentPlace.getSd());
	    	}
    	
    	// E(CP) = max(E(Pi)), where i ∈ [1, n]
    	int i;
    	LocalDateTime max = null;
    	for(i=0; i < childrenPlaces.size(); i++)
    	{
    		if(childrenPlaces.get(i).getEd() == null)
    			break;
    		
    		if (max == null)
    			max = childrenPlaces.get(i).getEd();
    		
    		if((childrenPlaces.get(i).getEd()).isAfter(max))
    			max = childrenPlaces.get(i).getEd();
    	}
    	if (i == childrenPlaces.size())
    		parentPlace.setEd(max);
    	
    	// D(CP) = max(D(Pi)), where i ∈ [1, n]
    	Long maxDuration = 0L;
    	for(i=0; i < childrenPlaces.size(); i++)
    	{
    		if(childrenPlaces.get(i).getDur() == null)
    			break;
    		   		
    		if(Long.compare(childrenPlaces.get(i).getDur(), maxDuration) > 0)
    			maxDuration = childrenPlaces.get(i).getDur();
    	}
    	if (i == childrenPlaces.size())
    		parentPlace.setDur(Long.valueOf(maxDuration));	
    }

	private static void calculationBetweenIterativeParentAndItsChildern(Place parentPlace,
    		ArrayList<Place> childrenPlaces)
    {
    	int n = 0;
    	if ((parentPlace.getDur() != null) && (childrenPlaces.get(1).getDur() != null))
    		n = (int) Math.ceil((float) parentPlace.getDur() / childrenPlaces.get(1).getDur());
    	    	
    	if ((n > 0) && (parentPlace.getSd() != null) && (parentPlace.getEd() != null))
		{
    		// S(P1) = E(P1) = S(CP)
			childrenPlaces.get(0).setSd(parentPlace.getSd());
			childrenPlaces.get(0).setEd(parentPlace.getSd());
			
			// S(Pn) = E(Pn) = S(CP)
			childrenPlaces.get(childrenPlaces.size()-1).setSd(parentPlace.getEd());
			childrenPlaces.get(childrenPlaces.size()-1).setEd(parentPlace.getEd());
			
			// S(P) = max(S(Pi)), where i ∈ [1, n]
			// E(P) = max(E(Pi)), where i ∈ [1, n]
			LocalDateTime sdP = parentPlace.getSd(), edP = sdP;
			for(int i=0; i<n; i++)
			{
				sdP = edP;
				edP = sdP.plusSeconds(childrenPlaces.get(1).getDur());
			}
			
			childrenPlaces.get(1).setSd(sdP);
			childrenPlaces.get(1).setEd(edP);
		}
    	
    }

	private static ArrayList<Place> getChildPlaceOfSequence(ArrayList<BpelThingsNetComponent> BpelThingsNetList, 
    		int startTransitionId, int endTransitionId)
    {
    	ArrayList<Place> placeList = new ArrayList<Place>();
    	
    	int id = startTransitionId;
    	for(int i=0; i < BpelThingsNetList.size(); i++)
    	{
    		if((BpelThingsNetList.get(i).getT1() != null) && (BpelThingsNetList.get(i).getT1().getId() == id))
    		{
    			placeList.add(BpelThingsNetList.get(i).getP());
    			
    			id = BpelThingsNetList.get(i).getT2().getId();
    		}
    	}
    	
    	return placeList;
    }
        
    private static ArrayList<Place> getChildPlaceOfFlow(ArrayList<BpelThingsNetComponent> BpelThingsNetList, 
    		int startTransitionId, int endTransitionId)
    {
    	ArrayList<Place> placeList = new ArrayList<Place>();
    	
    	for (int i=0; i < BpelThingsNetList.size(); i++)
    	{
    		if ((BpelThingsNetList.get(i).getT1() != null) 
    				&& (BpelThingsNetList.get(i).getT2() != null) 
    				&& (BpelThingsNetList.get(i).getT1().getId() == startTransitionId)
    				&& (BpelThingsNetList.get(i).getT2().getId() == endTransitionId))
    			placeList.add(BpelThingsNetList.get(i).getP());
    	}
    	
    	return placeList;
    }
    
}
