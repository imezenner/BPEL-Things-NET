package dz.usthb.wot.BPELThings_NET.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import dz.usthb.wot.BPELThings_NET.model.BpelThingsNetComponent;
import dz.usthb.wot.BPELThings_NET.model.Transition;
import dz.usthb.wot.BPELThings_NET.model.TransitionType;

public class CheckTemporalInformationBpelThingsNet {
	
	public static boolean launchTemporalVerificationBpelThingsNet (ArrayList<BpelThingsNetComponent> bpelThingsNetList,
    		LocalDateTime startDateProcess,
    		LocalDateTime endDateProcess)
    {
		return checkTemporalInformationBpelThingsNet (bpelThingsNetList,
        		geTransitionList(bpelThingsNetList),
        		getFirstTransition(bpelThingsNetList), 
        		getLastTransition(bpelThingsNetList), 
        		startDateProcess,
        		endDateProcess);
    }
	
	private static boolean checkTemporalInformationBpelThingsNet (ArrayList<BpelThingsNetComponent> bpelThingsNetList,
    		HashSet<Transition> transitionList,
    		Transition firstTransition, 
    		Transition lastTransition, 
    		LocalDateTime startDateProcess,
    		LocalDateTime endDateProcess)
    {
    	Iterator<Transition> itr = transitionList.iterator();
        while (itr.hasNext()) 
        {
        	Transition transition = itr.next();
            
            ArrayList<Transition> tl = getRelatedTransitions(bpelThingsNetList, 15, new ArrayList<Transition>());
            for (int i = 0; i < tl.size(); i++)
            	if (transition.getFd().isAfter(tl.get(i).getFd()))
            		return false;
        }
        
        if(firstTransition.getFd().isBefore(startDateProcess) || lastTransition.getFd().isAfter(endDateProcess))
        	return false;
        
    	return true;
    }
	
	private static ArrayList<Transition> getRelatedTransitions (ArrayList<BpelThingsNetComponent> bpelThingsNetList, 
    		int idTransition, 
    		ArrayList<Transition> t)
    {
    	ArrayList<Transition> transitionList = t;
    	for (int i = 0; i < bpelThingsNetList.size(); i++)
        {
    		BpelThingsNetComponent bpeThingsNetComponent = bpelThingsNetList.get(i);
    		if ((bpeThingsNetComponent.getT1() != null) && (bpeThingsNetComponent.getT1().getId() == idTransition))
    		{
    			if (bpeThingsNetComponent.getT2().getType() == TransitionType.Simple)
    				transitionList.add(bpeThingsNetComponent.getT2());
    			else
    				if (bpeThingsNetComponent.getT2().getType() == TransitionType.IF)
    					getRelatedTransitions (bpelThingsNetList, 
    							bpeThingsNetComponent.getT2().getId(), transitionList);
    		}
    			
        }
    	
    	return transitionList;
    }
	
	private static HashSet<Transition> geTransitionList (ArrayList<BpelThingsNetComponent> bpelThingsNetList)
    {
    	HashSet<Transition> transitionSet = new HashSet<Transition>();
    	for (int i = 0; i < bpelThingsNetList.size(); i++)
        {
    		if(bpelThingsNetList.get(i).getT1().getType() == TransitionType.Simple)
    			transitionSet.add(bpelThingsNetList.get(i).getT1());
    		
    		if (bpelThingsNetList.get(i).getT2().getType() == TransitionType.Simple)
    			transitionSet.add(bpelThingsNetList.get(i).getT2());
        }
    	return transitionSet;
    }
    
	private static Transition getFirstTransition (ArrayList<BpelThingsNetComponent> bpelThingsNetList)
    {
    	return bpelThingsNetList.get(0).getT1();
    }
    
	private static Transition getLastTransition (ArrayList<BpelThingsNetComponent> bpelThingsNetList)
    {
    	return bpelThingsNetList.get(bpelThingsNetList.size() - 1).getT2();
    }

}
