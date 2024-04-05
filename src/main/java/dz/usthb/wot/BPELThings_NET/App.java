package dz.usthb.wot.BPELThings_NET;

import dz.usthb.wot.BPELThings_NET.controller.Controller;
import dz.usthb.wot.BPELThings_NET.model.Model;
import dz.usthb.wot.BPELThings_NET.view.GraphicalUserInterface;

public class App
{
    public static void main(String[] args)
    {
    	Model model = new Model();
    	GraphicalUserInterface view = new GraphicalUserInterface(model);
    	new Controller(model, view);
    	
    }    
}
