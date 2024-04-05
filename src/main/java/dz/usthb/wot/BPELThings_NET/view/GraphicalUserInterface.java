package dz.usthb.wot.BPELThings_NET.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;

import dz.usthb.wot.BPELThings_NET.controller.Controller;
import dz.usthb.wot.BPELThings_NET.model.Model;

public class GraphicalUserInterface extends JFrame {
	
	public Model model;
	
	public JButton browseButton;
	public JLabel filePathLabel;
	public JButton submitButton;
	public JLabel resultLabel;
	
	public GraphicalUserInterface(Model model) {
		
		super();
		
		this.model = model;
		Controller controller = new Controller(model, this);
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SwingUtilities.updateComponentTreeUI(this);
		
		this.setTitle("BPEL Things-Net model");
		
		this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel contentPane1 = new JPanel();
        contentPane1.setLayout(new FlowLayout());
        contentPane1.setPreferredSize(new Dimension(400, 50));
        // Create a "Browse" button
        JLabel fileLabel = new JLabel("Enter BPEL file: ");
        browseButton = new JButton("Browse");
        // Add an action listener to the "Browse" button
        browseButton.addActionListener(controller);
        contentPane1.add(fileLabel);
        contentPane1.add(browseButton);
        // Create a label to display the file path
        if (model.getBpelFile() != null)
        	filePathLabel = new JLabel(model.getBpelFile().getName());
        else
        	filePathLabel = new JLabel("No File Selected");
        contentPane1.add(filePathLabel);
        this.add(contentPane1);
        
        JPanel contentPane2 = new JPanel();
        contentPane2.setLayout(new FlowLayout());
        contentPane2.setPreferredSize(new Dimension(400, 50)); 
        // Create a "submit" button
        submitButton = new JButton("Check");
        // Add an action listener to the submit button
        submitButton.addActionListener(controller);
        submitButton.setEnabled(false);
        contentPane2.add(submitButton);
        
        JPanel contentPane3 = new JPanel();
        contentPane3.setLayout(new FlowLayout()); 
        contentPane3.setPreferredSize(new Dimension(400, 50));
        contentPane3.setBorder(new TitledBorder("Result"));
        // Create a label to display the result
        resultLabel = new JLabel("");
        contentPane3.add(resultLabel);
        
        this.getContentPane().setLayout(new GridLayout(3, 1)); // Set the layout to GridLayout with 3 rows and 1 column
        this.getContentPane().add(contentPane1);
        this.getContentPane().add(contentPane2);
        this.getContentPane().add(contentPane3);
      

        // Set the size of the frame
        this.pack();
        
        this.setResizable(false);

        // Make the frame visible
        this.setVisible(true);
    }
	
}
