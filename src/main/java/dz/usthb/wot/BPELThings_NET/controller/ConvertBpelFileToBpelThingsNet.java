package dz.usthb.wot.BPELThings_NET.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import dz.usthb.wot.BPELThings_NET.model.BpelThingsNetComponent;
import dz.usthb.wot.BPELThings_NET.model.ChildReference;
import dz.usthb.wot.BPELThings_NET.model.Place;
import dz.usthb.wot.BPELThings_NET.model.PlaceType;
import dz.usthb.wot.BPELThings_NET.model.Transition;
import dz.usthb.wot.BPELThings_NET.model.TransitionType;
import dz.usthb.wot.BPELThings_NET.util.BpelElements;

public class ConvertBpelFileToBpelThingsNet {
	
	private LocalDateTime startDateProcess;
    private LocalDateTime endDateProcess;
    private Long durationProcess;
	private int idTransition;
	private ArrayList<BpelThingsNetComponent> bpelThingsNet;
	
	public ConvertBpelFileToBpelThingsNet() {
		this.idTransition = 0;
		this.bpelThingsNet = new ArrayList<BpelThingsNetComponent>();
	}
	
	public ConvertBpelFileToBpelThingsNet(int idTransition, ArrayList<BpelThingsNetComponent> bpelThingsNet) {
		this.idTransition = idTransition;
		this.bpelThingsNet = bpelThingsNet;
	}

	public int getIdTransition() {
		return idTransition;
	}

	public void setIdTransition(int idTransition) {
		this.idTransition = idTransition;
	}

	public ArrayList<BpelThingsNetComponent> getBpelThingsNet() {
		return bpelThingsNet;
	}

	public void setBpelThingsNet(ArrayList<BpelThingsNetComponent> bpelThingsNet) {
		this.bpelThingsNet = bpelThingsNet;
	}
	
	public LocalDateTime getStartDateProcess() {
		return startDateProcess;
	}

	public void setStartDateProcess(LocalDateTime startDateProcess) {
		this.startDateProcess = startDateProcess;
	}

	public LocalDateTime getEndDateProcess() {
		return endDateProcess;
	}

	public void setEndDateProcess(LocalDateTime endDateProcess) {
		this.endDateProcess = endDateProcess;
	}

	public Long getDurationProcess() {
		return durationProcess;
	}

	public void setDurationProcess(Long durationProcess) {
		this.durationProcess = durationProcess;
	}

	public void globalBpelProcessInformation (Node root)
    {
    	idTransition = idTransition + 1;
    	Transition t1 = new Transition(idTransition, TransitionType.Simple, null);
        idTransition = idTransition + 1;
        Transition t2 = new Transition(idTransition, TransitionType.Simple, null);
    	
    	//Transition t1=null, t2=null;
        
       startDateProcess = getStartDateBpelElement(root);
       endDateProcess = getEndDateBpelElement(root);
       durationProcess = getDurationBpelElement(root);
       convertBpelElementToBpelThingsNetComponent(root.getNodeName(), t1, t2,
        		getIdBpelElement(root),
        		startDateProcess,
        		endDateProcess,
        		durationProcess);
    }

	public void launchConversionBpelProcessToBpelThingsNetModel (Document doc, int index)
    {
    	if (index >= bpelThingsNet.size())
			return;

    	BpelThingsNetComponent p = bpelThingsNet.get(index);
    	if (p == null)
			return;

    	Node n = doc.getElementById(String.valueOf(p.getP().getId()));

    	if ((n != null) && (n.hasChildNodes()))
    	{
    		ChildReference childReference = null;
    		if (n.getNodeName().equalsIgnoreCase("bpel:process") || n.getNodeName().equalsIgnoreCase("bpel:sequence"))
    		{
    			childReference = sequenceChild(n.getChildNodes());
    			p.setReferenceToChildPlace(childReference);
    		}
    		else
    			if (n.getNodeName().equalsIgnoreCase("bpel:flow"))
    			{
    				childReference = flowChild(n.getChildNodes());
    				p.setReferenceToChildPlace(childReference);
    			}
    			else
    				if (n.getNodeName().equalsIgnoreCase("bpel:if"))
    				{
    					childReference = ifChild(n.getChildNodes());
    					p.setReferenceToChildPlace(childReference);
    				}
    				else
        				if (n.getNodeName().equalsIgnoreCase("bpel:while"))
        				{
        					childReference = whileChild(n, n.getChildNodes());
        					p.setReferenceToChildPlace(childReference);
        				}
        				else
            				if (n.getNodeName().equalsIgnoreCase("bpel:repeatUntil"))
            				{
            					childReference = repeatUntilChild(n, n.getChildNodes());
            					p.setReferenceToChildPlace(childReference);
            				}
    	}

    	launchConversionBpelProcessToBpelThingsNetModel (doc, index + 1);
    }

	private void convertBpelElementToBpelThingsNetComponent (String bpelElementName,
    		Transition t1, Transition t2, int idValue, LocalDateTime sd, LocalDateTime ed, Long dur)
    {
    	switch (bpelElementName)
    	{
    	case "bpel:process"    : bpelThingsNet.add(new BpelThingsNetComponent(t1, new Place(idValue, "R", PlaceType.Composite, sd, ed, dur), t2, null)); break;
    	case "bpel:assign"     : bpelThingsNet.add(new BpelThingsNetComponent(t1, new Place(idValue, "ASSIGN", PlaceType.Ordinary, sd, ed, Long.valueOf(0)), t2, null)); break;
    	case "bpel:wait"       : bpelThingsNet.add(new BpelThingsNetComponent(t1, new Place(idValue, "WAIT", PlaceType.Virtual, sd, ed, dur), t2, null)); break;
    	case "bpel:exit"       : bpelThingsNet.add(new BpelThingsNetComponent(t1, new Place(idValue, "EXIT", PlaceType.Ordinary, sd, ed, Long.valueOf(0)), t2, null)); break;
    	case "bpel:empty"      : bpelThingsNet.add(new BpelThingsNetComponent(t1, new Place(idValue, "EMPTY", PlaceType.Ordinary, sd, ed, Long.valueOf(0)), t2, null)); break;
    	case "wot:get"         : bpelThingsNet.add(new BpelThingsNetComponent(t1, new Place(idValue, "GET", PlaceType.Ordinary, sd, ed, dur), t2, null)); break;
    	case "wot:post"        : bpelThingsNet.add(new BpelThingsNetComponent(t1, new Place(idValue, "POST", PlaceType.Ordinary, sd, ed, dur), t2, null)); break;
    	case "wot:put"         : bpelThingsNet.add(new BpelThingsNetComponent(t1, new Place(idValue, "PUT", PlaceType.Ordinary, sd, ed, dur), t2, null)); break;
    	case "wot:delete"      : bpelThingsNet.add(new BpelThingsNetComponent(t1, new Place(idValue, "DELETE", PlaceType.Ordinary, sd, ed, dur), t2, null)); break;
    	case "bpel:sequence"   : bpelThingsNet.add(new BpelThingsNetComponent(t1, new Place(idValue, "SEQ", PlaceType.Composite, sd, ed, dur), t2, null)); break;
    	case "bpel:flow"       : bpelThingsNet.add(new BpelThingsNetComponent(t1, new Place(idValue, "PAR", PlaceType.Composite, sd, ed, dur), t2, null)); break;
    	case "bpel:if"         : bpelThingsNet.add(new BpelThingsNetComponent(t1, new Place(idValue, "IF", PlaceType.Composite, sd, ed, dur), t2, null)); break;
    	case "bpel:while"      : bpelThingsNet.add(new BpelThingsNetComponent(t1, new Place(idValue, "WHILE", PlaceType.Composite, sd, ed, dur), t2, null)); break;
    	case "bpel:repeatUntil": bpelThingsNet.add(new BpelThingsNetComponent(t1, new Place(idValue, "REPEATUNTIL", PlaceType.Composite, sd, ed, dur), t2, null)); break;
    	case "virtualNode": bpelThingsNet.add(new BpelThingsNetComponent(t1, new Place(idValue, "", PlaceType.Virtual, sd, ed, dur), t2, null)); break;
    	}
    }
	
	private ChildReference sequenceChild(NodeList nodeList)
    {
    	idTransition = idTransition + 1;
    	Transition t1 = new Transition(idTransition, TransitionType.Simple, null);

    	Transition t2 = t1, t3 = null;
    	for (int temp = 0; temp < nodeList.getLength(); temp++)
        {
        	Node node = nodeList.item(temp);
        	if (node.getNodeType() == Node.ELEMENT_NODE)
        	{
        		if (BpelElements.bpelElementsList.contains(node.getNodeName().toLowerCase()))
        		{
                    idTransition = idTransition + 1;
                    t3 = new Transition(idTransition, TransitionType.Simple, null);
                    convertBpelElementToBpelThingsNetComponent(node.getNodeName(), t2, t3,
                    		getIdBpelElement(node),
                    		getStartDateBpelElement(node),
                    		getEndDateBpelElement(node),
                    		getDurationBpelElement(node));

                    t2 = t3;
        		}
        	}
        }

    	return new ChildReference(t1.getId(), t3.getId());
    }
	
	private ChildReference flowChild(NodeList nodeList)
    {
    	idTransition = idTransition + 1;
    	Transition t1 = new Transition(idTransition, TransitionType.Simple, null);

    	idTransition = idTransition + 1;
        Transition t2 = new Transition(idTransition, TransitionType.Simple, null);

    	for (int temp = 0; temp < nodeList.getLength(); temp++)
        {
        	Node node = nodeList.item(temp);
        	if ((node.getNodeType() == Node.ELEMENT_NODE) && (BpelElements.bpelElementsList.contains(node.getNodeName().toLowerCase())))
        		convertBpelElementToBpelThingsNetComponent(node.getNodeName(), t1, t2,
                		getIdBpelElement(node),
                		getStartDateBpelElement(node),
                		getEndDateBpelElement(node),
                		getDurationBpelElement(node));
        }

    	return new ChildReference(t1.getId(), t2.getId());
    }

    private ChildReference ifChild(NodeList nodeList)
    {
    	idTransition = idTransition + 1;
    	Transition t1 = new Transition(idTransition, TransitionType.IF, null);

    	idTransition = idTransition + 1;
        Transition t2 = new Transition(idTransition, TransitionType.Simple, null);

        int temp;
        Node nodeIf = null;
        boolean thereIsElse = false;
        for (temp = 0; temp < nodeList.getLength(); temp++)
        {
        	Node node = nodeList.item(temp);
        	if (node.getNodeType() == Node.ELEMENT_NODE)
        	{
        		if (node.getNodeName().equalsIgnoreCase("condition"))
        		{
        			System.out.println(node.getTextContent());
        		}
        		else
        			if (BpelElements.bpelElementsList.contains(node.getNodeName().toLowerCase()))
            		{
        				nodeIf = node;
        				convertBpelElementToBpelThingsNetComponent(node.getNodeName(), t1, t2,
                        		getIdBpelElement(node),
                        		getStartDateBpelElement(node),
                        		getEndDateBpelElement(node),
                        		getDurationBpelElement(node));
            		}
        			else
        				if (node.getNodeName().equalsIgnoreCase("else"))
                		{
        					thereIsElse = true;
        					NodeList nodeListElse = node.getChildNodes();
        					for (int tempElse = 0; tempElse < nodeListElse.getLength(); tempElse++)
        			        {
        						Node nodeElse = nodeListElse.item(tempElse);
        						if (BpelElements.bpelElementsList.contains(nodeElse.getNodeName().toLowerCase()))
                        		{
        							convertBpelElementToBpelThingsNetComponent(nodeElse.getNodeName(), t1, t2,
            	                    		getIdBpelElement(nodeElse),
            	                    		getStartDateBpelElement(nodeElse),
            	                    		getEndDateBpelElement(nodeElse),
            	                    		getDurationBpelElement(nodeElse));
                        		}
        			        }
                		}
        	}
        }

        // if ... then ...
        if (!thereIsElse) {
        	convertBpelElementToBpelThingsNetComponent("virtualNode", t1, t2,
            		0,
            		getStartDateBpelElement(nodeIf),
            		getStartDateBpelElement(nodeIf),
            		Long.valueOf(0));
		}

        return new ChildReference(t1.getId(), t2.getId());
    }

    private ChildReference whileChild(Node node, NodeList nodeList)
    {
    	idTransition = idTransition + 1;
    	Transition t1 = new Transition(idTransition, TransitionType.Simple, null);

    	idTransition = idTransition + 1;
    	Transition t2 = new Transition(idTransition, TransitionType.IF, null);
    	convertBpelElementToBpelThingsNetComponent("virtualNode", t1, t2,
        		0,
        		getStartDateBpelElement(node),
        		getStartDateBpelElement(node),
        		Long.valueOf(0));

    	Transition t3 = null;
    	for (int temp = 0; temp < nodeList.getLength(); temp++)
        {
        	Node childNode = nodeList.item(temp);
        	if (childNode.getNodeType() == Node.ELEMENT_NODE)
        	{
        		if (childNode.getNodeName().equalsIgnoreCase("condition"))
        		{
        			System.out.println(childNode.getTextContent());
        		}
        		else
        			if (BpelElements.bpelElementsList.contains(childNode.getNodeName().toLowerCase()))
            		{
                        idTransition = idTransition + 1;
                        t3 = new Transition(idTransition, TransitionType.Simple, null);
                        convertBpelElementToBpelThingsNetComponent(childNode.getNodeName(), t2, t3,
                        		getIdBpelElement(childNode),
                        		getStartDateBpelElement(childNode),
                        		getEndDateBpelElement(childNode),
                        		getDurationBpelElement(childNode));
            		}
        	}
        }

    	idTransition = idTransition + 1;
        Transition t4 = new Transition(idTransition, TransitionType.Simple, null);
        convertBpelElementToBpelThingsNetComponent("virtualNode", t3, t4,
        		0,
        		getEndDateBpelElement(node),
        		getEndDateBpelElement(node),
        		Long.valueOf(0));

    	return new ChildReference(t1.getId(), t4.getId());
    }

    private ChildReference repeatUntilChild(Node node, NodeList nodeList)
    {
    	idTransition = idTransition + 1;
    	Transition t1 = new Transition(idTransition, TransitionType.Simple, null);

    	idTransition = idTransition + 1;
    	Transition t2 = new Transition(idTransition, TransitionType.Simple, null);
    	convertBpelElementToBpelThingsNetComponent("virtualNode", t1, t2,
        		0,
        		getStartDateBpelElement(node),
        		getStartDateBpelElement(node),
        		Long.valueOf(0));
    	
    	Transition t3 = null;
    	for (int temp = 0; temp < nodeList.getLength(); temp++)
        {
        	Node childNode = nodeList.item(temp);
        	if (childNode.getNodeType() == Node.ELEMENT_NODE)
        	{
        		if (childNode.getNodeName().equalsIgnoreCase("condition"))
        		{
        			System.out.println(childNode.getTextContent());
        		}
        		else
        			if (BpelElements.bpelElementsList.contains(childNode.getNodeName().toLowerCase()))
            		{
                        idTransition = idTransition + 1;
                        t3 = new Transition(idTransition, TransitionType.IF, null);
                        convertBpelElementToBpelThingsNetComponent(childNode.getNodeName(), t2, t3,
                        		getIdBpelElement(childNode),
                        		getStartDateBpelElement(childNode),
                        		getEndDateBpelElement(childNode),
                        		getDurationBpelElement(childNode));
            		}
        	}
        }
    	
    	idTransition = idTransition + 1;
        Transition t4 = new Transition(idTransition, TransitionType.Simple, null);
        convertBpelElementToBpelThingsNetComponent("virtualNode", t3, t4,
        		0,
        		getEndDateBpelElement(node),
        		getEndDateBpelElement(node),
        		Long.valueOf(0));

    	return new ChildReference(t1.getId(), t4.getId());
    }
    
    private int getIdBpelElement(Node node)
    {
    	Node exist = null;
    	return ((exist = node.getAttributes().getNamedItem("id")) != null ? Integer.parseInt(exist.getTextContent()) : 0);
    }

    private LocalDateTime getStartDateBpelElement(Node node)
    {
    	Node exist = null;
    	return ((exist = node.getAttributes().getNamedItem("wot:SD")) != null ? parseDateTime(exist.getTextContent()) : null);
    }

    private LocalDateTime getEndDateBpelElement(Node node)
    {
    	Node exist = null;
    	return ((exist = node.getAttributes().getNamedItem("wot:ED")) != null ? parseDateTime(exist.getTextContent()) : null);
    }

    private Long getDurationBpelElement(Node node)
    {
    	Node exist = null;
    	return ((exist = node.getAttributes().getNamedItem("wot:DUR")) != null ? convertBpelDurationToSecond(exist.getTextContent()) : null);
    }
    
    private long convertBpelDurationToSecond (String bpelDuration)
    {
    	// Duration format PnYnMnDTnHnMnS
    	int years = 0, months = 0, days = 0, hours = 0, minutes = 0, seconds = 0;
    	try
    	{
			Duration dur = DatatypeFactory.newInstance().newDuration(bpelDuration);
			years = dur.getYears();
			months = dur.getMonths();
			days = dur.getDays();
			hours = dur.getHours();
			minutes = dur.getMinutes();
			seconds = dur.getSeconds();
		}
    	catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return years * 31556952L + (months * 31556952L / 12) + days * 86400 + hours * 3600 + minutes * 60 + seconds;
    }
    
    private LocalDateTime parseDateTime (String bpelDateTime)
    {
    	return LocalDateTime.parse(bpelDateTime);
    }


}
