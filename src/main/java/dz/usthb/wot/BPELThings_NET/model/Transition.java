package dz.usthb.wot.BPELThings_NET.model;

import java.time.LocalDateTime;

public class Transition {

	private int id;
	private TransitionType type;
	private LocalDateTime fd;

	public Transition() {
	}

	public Transition(int id, TransitionType type, LocalDateTime fd) {
		this.id = id;
		this.type = type;
		this.fd = fd;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public TransitionType getType() {
		return type;
	}

	public void setType(TransitionType type) {
		this.type = type;
	}

	public LocalDateTime getFd() {
		return fd;
	}

	public void setFd(LocalDateTime fd) {
		this.fd = fd;
	}
	
	@Override
	public int hashCode() {
	    return id;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj)
			return true;
		
		if (obj == null)
			return false;
		
		if (getClass() != obj.getClass())
			return false;
		
		Transition other = (Transition) obj;
		return ((id == other.id) && (type.compareTo(other.type) == 0) && (fd.isEqual(other.fd)));
			
		
	}

}
