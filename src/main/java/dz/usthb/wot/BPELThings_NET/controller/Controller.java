package dz.usthb.wot.BPELThings_NET.controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.w3c.dom.Document;

import dz.usthb.wot.BPELThings_NET.model.Model;
import dz.usthb.wot.BPELThings_NET.model.BpelThingsNetComponent;
import dz.usthb.wot.BPELThings_NET.view.GraphicalUserInterface;

public class Controller implements ActionListener {
	
	private GraphicalUserInterface view;
	private Model model;
	
	public Controller(Model model, GraphicalUserInterface view) {
		this.model = model;
		this.view = view;
	}
	
	private boolean openBpelFile()
	{
		JFileChooser jfc = new JFileChooser();
		jfc.setFileFilter(new BpelFileFilter());		
		int returnValue = jfc.showOpenDialog(view);
		
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = jfc.getSelectedFile();
			
			String extension = selectedFile.getName().substring(selectedFile.getName().lastIndexOf(".") + 1);
		    if (!extension.equalsIgnoreCase("bpel")) 
		    {
		        JOptionPane.showMessageDialog(null, "Please select a BPEL file.");
		        return false;
		    }
		    else
		    {
		    	model.setBpelFile(selectedFile);
				view.filePathLabel.setText(selectedFile.getName());
				return true;
		    }
		}
		else
			return false;
	}
	
	private boolean checkBpelFile()
	{
		Document doc = ReadInputBpelFile.prepareBpelFile(model.getBpelFile().getAbsolutePath());
    	
    	ArrayList<BpelThingsNetComponent> bpelThingsNet = new ArrayList<>();
        LocalDateTime startDateProcess;
        LocalDateTime endDateProcess;
    	ConvertBpelFileToBpelThingsNet c = new ConvertBpelFileToBpelThingsNet();
        c.globalBpelProcessInformation(doc.getDocumentElement());
        c.launchConversionBpelProcessToBpelThingsNetModel(doc, 0);
        bpelThingsNet = c.getBpelThingsNet();
        startDateProcess = c.getStartDateProcess();
        endDateProcess = c.getEndDateProcess();
        
        CompleteTemporalInformationBpelThingsNetModel.calculationTemporalInformationBpelThingsNetModel(bpelThingsNet);

        AbstractBpelThingsNetToFlattenModel.launchAbtractionProcess(bpelThingsNet);
        
        CalculateTransitionBpelThingsNet.calculationTransitionBpelThingsNet(bpelThingsNet);
        
        return CheckTemporalInformationBpelThingsNet.launchTemporalVerificationBpelThingsNet(bpelThingsNet, startDateProcess, endDateProcess);	   
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == view.browseButton)
		{
			view.resultLabel.setText("");
			view.filePathLabel.setText("No File Selected");
			if (openBpelFile() == true)
				view.submitButton.setEnabled(true);
			else
				view.submitButton.setEnabled(false);
		}
		else
			if(e.getSource() == view.submitButton)
			{
				if (checkBpelFile() == true)
				{
					view.resultLabel.setText("BPEL process is temporally valid");
					view.resultLabel.setForeground(new Color(0,153,0));

				}
				else
					{
						view.resultLabel.setText("BPEL process is temporally invalid");
						view.resultLabel.setForeground(Color.RED);
					}
			
			}        
    }

}
