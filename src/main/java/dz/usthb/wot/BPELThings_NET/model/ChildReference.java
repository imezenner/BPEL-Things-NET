package dz.usthb.wot.BPELThings_NET.model;

public class ChildReference {

	private int idFirstTransition;
	private int idLastTransition;

	public ChildReference() {
	}

	public ChildReference(int idFirstTransition, int idLastTransition) {
		this.idFirstTransition = idFirstTransition;
		this.idLastTransition = idLastTransition;
	}

	public int getIdFirstTransition() {
		return idFirstTransition;
	}

	public void setIdFirstTransition(int idFirstTransition) {
		this.idFirstTransition = idFirstTransition;
	}

	public int getIdLastTransition() {
		return idLastTransition;
	}

	public void setIdLastTransition(int idLastTransition) {
		this.idLastTransition = idLastTransition;
	}

}
