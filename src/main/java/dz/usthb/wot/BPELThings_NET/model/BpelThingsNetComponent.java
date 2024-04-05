package dz.usthb.wot.BPELThings_NET.model;

public class BpelThingsNetComponent {

	private Transition t1;
	private Place p;
	private Transition t2;
	private ChildReference referenceToChildPlace;

	public BpelThingsNetComponent() {
	}

	public BpelThingsNetComponent(Transition t1, Place p, Transition t2, ChildReference referenceToChildPlace) {
		this.t1 = t1;
		this.p = p;
		this.t2 = t2;
		this.referenceToChildPlace = referenceToChildPlace;
	}

	public Transition getT1() {
		return t1;
	}

	public void setT1(Transition t1) {
		this.t1 = t1;
	}

	public Place getP() {
		return p;
	}

	public void setP(Place p) {
		this.p = p;
	}

	public Transition getT2() {
		return t2;
	}

	public void setT2(Transition t2) {
		this.t2 = t2;
	}

	public ChildReference getReferenceToChildPlace() {
		return referenceToChildPlace;
	}

	public void setReferenceToChildPlace(ChildReference referenceToChildPlace) {
		this.referenceToChildPlace = referenceToChildPlace;
	}
}
