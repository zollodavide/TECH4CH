package it.uniroma3.tech4ch.model;

import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "position")
public class Position {
	
	@Id
	@Column(name = "id_position",  unique = true)
	private Integer id;
	
	@Column(name = "id_visitor")
	private Integer visitorId; 
	
	@Column(name = "poi_name")
	private String poi_name;
	
    @Column(name = "start_time", nullable = false)
	private LocalTime start_time;

    @Column(name = "finish_time", nullable = false)
	private LocalTime end_time;
    
    public Position() {}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getVisitor_id() {
		return visitorId;
	}

	public void setVisitor_id(Integer visitor_id) {
		this.visitorId = visitor_id;
	}

	public String getPoi_name() {
		return poi_name;
	}

	public void setPoi_name(String poi_name) {
		this.poi_name = poi_name;
	}

	public LocalTime getStart_time() {
		return start_time;
	}

	public void setStart_time(LocalTime start_time) {
		this.start_time = start_time;
	}

	public LocalTime getEnd_time() {
		return end_time;
	}

	public void setEnd_time(LocalTime end_time) {
		this.end_time = end_time;
	}
	
    
}
