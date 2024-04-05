package dz.usthb.wot.BPELThings_NET.controller;

import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import dz.usthb.wot.BPELThings_NET.util.BpelElements;

public class ReadInputBpelFile {
	
	private static int id = 1;
	
	public static Document prepareBpelFile (String filePath)
	{
		// Instantiate the Factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Document doc = null;
        
		try {
        	// optional, but recommended
            // process XML securely, avoid attacks like XML External Entities (XXE)
			dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

			// parse XML file
	        DocumentBuilder db = dbf.newDocumentBuilder();
	        doc = db.parse(new File(filePath));

	        // optional, but recommended
	        // http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
	        doc.getDocumentElement().normalize();
	        
	        setIdForAllBpelElements (doc);
	        
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return doc;
	}
	
	private static void setIdForAllBpelElements (Document doc)
    {
    	NodeList nodeList = doc.getElementsByTagName("*");
        for (int temp = 0; temp < nodeList.getLength(); temp++)
        {
            Node node = nodeList.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
            	if (BpelElements.bpelElementsList.contains(node.getNodeName().toLowerCase()))
        		{
            		Element element = (Element) node;
                    // System.out.println("Element Name : " + element.getNodeName());

                    element.setAttribute("id", String.valueOf(id));
            		element.setIdAttribute("id", true);

            		id ++;
        		}

            }
        }
    }

}
