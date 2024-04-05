package dz.usthb.wot.BPELThings_NET.controller;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class BpelFileFilter extends FileFilter{
	
	public BpelFileFilter() 
	{
		super();
	}
	
	@Override
    public boolean accept(File f) 
    {
        if (f.isDirectory())
            return true;
        
        String extension = f.getName().toLowerCase();
        
        return extension.endsWith(".bpel");
    }
   
	@Override
    public String getDescription() 
    {
    	return "BPEL file";
    }

}
