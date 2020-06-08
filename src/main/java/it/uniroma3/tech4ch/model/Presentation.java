package it.uniroma3.tech4ch.model;

import java.time.LocalTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "presentation")
public class Presentation {
	
	@Id
    @Column(name = "id_presentation", unique = true)
	private Integer id;
    
    @Column(name = "id_visitor", nullable = false)
	private Integer visitorId;
	
    @Column(name = "poi_name", nullable = false)
    private String poiName;
	
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;	
    
    @Column(name = "finish_time", nullable = false)
    private LocalTime endTime;
    
    @Column(name = "terminatedby", nullable = false)
    private String terminatedBy;
    
    public Presentation() {}

	Integer getId() {
		return id;
	}

	void setId(Integer id) {
		this.id = id;
	}

	Integer getVisitorId() {
		return visitorId;
	}

	void setVisitorId(Integer visitorId) {
		this.visitorId = visitorId;
	}

	String getPoiName() {
		return poiName;
	}

	void setPoiName(String poiName) {
		this.poiName = poiName;
	}

	LocalTime getStartTime() {
		return startTime;
	}

	void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	LocalTime getEndTime() {
		return endTime;
	}

	void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	String getTerminatedBy() {
		return terminatedBy;
	}

	void setTerminatedBy(String terminatedBy) {
		this.terminatedBy = terminatedBy;
	}
	
    
    

}
