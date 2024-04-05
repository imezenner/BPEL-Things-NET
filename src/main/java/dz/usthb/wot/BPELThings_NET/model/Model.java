package dz.usthb.wot.BPELThings_NET.model;

import java.io.File;

public class Model {
	
	private File bpelFile;
	
	public Model() {
	}

	public Model(File bpelFile) {
		this.bpelFile = bpelFile;
	}

	public File getBpelFile() {
		return bpelFile;
	}

	public void setBpelFile(File bpelFile) {
		this.bpelFile = bpelFile;
	}

}
