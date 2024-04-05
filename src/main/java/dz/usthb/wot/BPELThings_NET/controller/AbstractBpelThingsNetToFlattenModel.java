package dz.usthb.wot.BPELThings_NET.controller;

import java.util.ArrayList;

import dz.usthb.wot.BPELThings_NET.model.BpelThingsNetComponent;
import dz.usthb.wot.BPELThings_NET.model.TransitionType;

public class AbstractBpelThingsNetToFlattenModel {
	
	public static void launchAbtractionProcess (ArrayList<BpelThingsNetComponent> bpelThingsNetList)
    {
    	ArrayList<BpelThingsNetComponent> subnetElements;
    	for (int i = bpelThingsNetList.size()-1; i >= 0; i--)
        {
    		subnetElements = null;
    		BpelThingsNetComponent bpelThingsNetComponent = bpelThingsNetList.get(i);   
    		
    		if (bpelThingsNetComponent.getReferenceToChildPlace() != null)
			{
    			int firstTransitionAfter = 0, lastTransitionAfter = 0;
    			if (bpelThingsNetComponent.getT1() != null)
    				firstTransitionAfter = bpelThingsNetComponent.getT1().getId();
    			if (bpelThingsNetComponent.getT2() != null)
    				lastTransitionAfter = bpelThingsNetComponent.getT2().getId();
    			
    			String typeFirstTransitionAfter = "", typeLastTransitionAfter = "";
    			if (bpelThingsNetComponent.getT1() != null)
    				typeFirstTransitionAfter = bpelThingsNetComponent.getT1().getType().name();
    			if (bpelThingsNetComponent.getT2() != null)
    				typeLastTransitionAfter = bpelThingsNetComponent.getT2().getType().name();
    			
				int firstTransitionBefore = bpelThingsNetComponent.getReferenceToChildPlace().getIdFirstTransition();
    			int lastTransitionBefore = bpelThingsNetComponent.getReferenceToChildPlace().getIdLastTransition();
    			
    			if ((bpelThingsNetComponent.getP().getName() == "R") 
    					|| (bpelThingsNetComponent.getP().getName() == "SEQ") 
    					|| (bpelThingsNetComponent.getP().getName() == "WHILE") 
    					|| (bpelThingsNetComponent.getP().getName() == "REPEATUNTIL"))
    				subnetElements = getChildrenOfSequence(bpelThingsNetList, 
	    					firstTransitionBefore, 
	    					lastTransitionBefore);
    			else
    				if ((bpelThingsNetComponent.getP().getName() == "PAR") 
    						|| (bpelThingsNetComponent.getP().getName() == "IF"))
    					subnetElements = getChildrenfFlow(bpelThingsNetList, 
            					firstTransitionBefore,
            					lastTransitionBefore);
    			
    			if (subnetElements != null)
    			{
    				replaceCompositePlaceByEquivalentSubnet(bpelThingsNetList, i, subnetElements);
        				
        			mergeAdjacentTransition (bpelThingsNetList, 
        					firstTransitionBefore, 
        					firstTransitionAfter,
        					typeFirstTransitionAfter,
        					lastTransitionBefore,
        					lastTransitionAfter,
        					typeLastTransitionAfter);
    			}
			}
        }    	
    }
	
	public static ArrayList<BpelThingsNetComponent> getChildrenOfSequence(ArrayList<BpelThingsNetComponent> bpelThingsNetList, 
    		int startTransitionId, int endTransitionId)
    {
    	ArrayList<BpelThingsNetComponent> childElement = new ArrayList<BpelThingsNetComponent>();
    	
    	int id = startTransitionId;
    	for(int i=0; i < bpelThingsNetList.size(); i++)
    	{
    		if((bpelThingsNetList.get(i).getT1() != null) && (bpelThingsNetList.get(i).getT1().getId() == id))
    		{
    			childElement.add(bpelThingsNetList.get(i));
    			
    			id = bpelThingsNetList.get(i).getT2().getId();
    		}
    	}
    	
    	return childElement;
    }

	public static ArrayList<BpelThingsNetComponent> getChildrenfFlow(ArrayList<BpelThingsNetComponent> bpelThingsNetList, 
    		int startTransitionId, 
    		int endTransitionId)
    {
    	ArrayList<BpelThingsNetComponent> childElement = new ArrayList<BpelThingsNetComponent>();
    	
    	for (int i=0; i < bpelThingsNetList.size(); i++)
    	{
    		if ((bpelThingsNetList.get(i).getT1() != null) 
    				&& (bpelThingsNetList.get(i).getT2() != null)
    				&& (bpelThingsNetList.get(i).getT1().getId() == startTransitionId) 
    				&& (bpelThingsNetList.get(i).getT2().getId() == endTransitionId))
    			childElement.add(bpelThingsNetList.get(i));
    	}
    	
    	return childElement;
    }
	
	private static void replaceCompositePlaceByEquivalentSubnet (ArrayList<BpelThingsNetComponent> bpelThingsNetList,
    		int index,
    		ArrayList<BpelThingsNetComponent> subnetElements)
    {
		bpelThingsNetList.removeAll(subnetElements);
		bpelThingsNetList.remove(index);
		
		bpelThingsNetList.addAll(index, subnetElements);
    }
	
	private static void mergeAdjacentTransition (ArrayList<BpelThingsNetComponent> bpelThingsNetList, 
    		int firstTransitionBefore,
    		int firstTransitionAfter,
    		String typeFirstTransitionAfter,
    		int lastTransitionBefore,
    		int lastTransitionAfter,
    		String typeLastTransitionAfter)
    {
    	for (int j=0; j < bpelThingsNetList.size(); j++)
		{
			if((firstTransitionAfter > 0) 
					&& (!typeFirstTransitionAfter.isEmpty()) && (typeFirstTransitionAfter != null)
					&& (bpelThingsNetList.get(j).getT1() != null) 
					&& (bpelThingsNetList.get(j).getT1().getId() == firstTransitionBefore))
			{
				bpelThingsNetList.get(j).getT1().setId(firstTransitionAfter);
				if(typeFirstTransitionAfter == "Simple")
					bpelThingsNetList.get(j).getT1().setType(TransitionType.Simple);
				else
					if(typeFirstTransitionAfter == "IF")
						bpelThingsNetList.get(j).getT1().setType(TransitionType.IF);
			}
				
			
			if((lastTransitionAfter > 0)
					&& (!typeLastTransitionAfter.isEmpty()) && (typeLastTransitionAfter != null)
					&& (bpelThingsNetList.get(j).getT2() != null)
					&& (bpelThingsNetList.get(j).getT2().getId() == lastTransitionBefore))
			{
				bpelThingsNetList.get(j).getT2().setId(lastTransitionAfter);
				
				if(typeLastTransitionAfter == "Simple")
					bpelThingsNetList.get(j).getT2().setType(TransitionType.Simple);
				else
					if(typeLastTransitionAfter == "IF")
						bpelThingsNetList.get(j).getT2().setType(TransitionType.IF);
			}
		}
    }


}
