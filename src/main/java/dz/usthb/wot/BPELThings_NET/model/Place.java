package dz.usthb.wot.BPELThings_NET.model;

import java.time.LocalDateTime;

public class Place {

	private int id;
	private String name;
	private PlaceType type;
	private LocalDateTime sd;
	private LocalDateTime ed;
	private Long dur;

	public Place() {
	}

	public Place(int id, String name, PlaceType type, LocalDateTime sd, LocalDateTime ed, Long dur) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.sd = sd;
		this.ed = ed;
		this.dur = dur;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PlaceType getType() {
		return type;
	}

	public void setType(PlaceType type) {
		this.type = type;
	}

	public LocalDateTime getSd() {
		return sd;
	}

	public void setSd(LocalDateTime sd) {
		this.sd = sd;
	}

	public LocalDateTime getEd() {
		return ed;
	}

	public void setEd(LocalDateTime ed) {
		this.ed = ed;
	}

	public Long getDur() {
		return dur;
	}

	public void setDur(Long dur) {
		this.dur = dur;
	}

}
