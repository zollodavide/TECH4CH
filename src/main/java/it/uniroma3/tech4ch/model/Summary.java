package it.uniroma3.tech4ch.model;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import it.uniroma3.tech4ch.service.PositionService;
import it.uniroma3.tech4ch.service.PresentationService;
import it.uniroma3.tech4ch.service.VisitorService;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Summary {

	PositionService positionService;
	VisitorService visitorService;
	PresentationService presentationService;
	
	private List<Position> visited_poi;
	private List<Visitor> visitors;
	private List<Presentation> watched_presentations;
	
	private Long minutesSpent;
	private Integer minutesWatched;
	private Boolean presentationInterrupt;
	
	private Boolean watchedMoreThanAvg;
	private Boolean stayedMoreThanAvg;
	
	private Map<String,Long> poi2stayTime;
	
	//0 DIDN'T ENJOY
	//1 ENJOYED A LITTLE BIT
	//2 ENJOYED THE VISIT
	//3 ENJOYED A LOT
	private Integer sentimentPoints;

	
	
	public Summary(Visitor vis, Statistics statistiche, VisitorService visitorService ,PositionService positionService, PresentationService presentationService) {
		this.positionService = positionService;
		this.presentationService = presentationService;
		this.visitorService = visitorService;

		visitors = new ArrayList<Visitor>();
		List<Visitor> group = visitorService.getGroup(vis.getGroup());
		visitors.addAll(group);
		
		
		visited_poi = positionService.getByVisitorId(vis.getId());
		watched_presentations = presentationService.getByVisitorId(vis.getId());
		createSummary(vis);
		estimateSentiment(statistiche);
	}

	public Summary(List<Visitor> group, Statistics statistiche, VisitorService visitorService, PositionService positionService, PresentationService presentationService) {
		this.positionService = positionService;
		this.presentationService = presentationService;
		this.visitorService = visitorService;
		
		visitors = new ArrayList<Visitor>(group);
		visited_poi = positionService.getByVisitorId(visitors.get(0).getId());
		watched_presentations = presentationService.getByVisitorId(visitors.get(0).getId()); 
		
		createSummary(group.get(0));
		estimateSentiment(statistiche);
	}

	
	private void estimateSentiment(Statistics statistiche) {
		stayedMoreThanAvg = getMinutesSpent() > statistiche.getAverageGroupStay();
		watchedMoreThanAvg = getWatched_presentations().size() > statistiche.getaveragePresentationsWatched();
		sentimentPoints = 0;
		
		if(watchedMoreThanAvg)
			sentimentPoints++;
		if(stayedMoreThanAvg)
			sentimentPoints++;
		if(!presentationInterrupt)
			sentimentPoints++;
	}
	
	private void createSummary(Visitor vis) {
		minutesSpent = vis.getStartTime().until(vis.getEndTime(), ChronoUnit.MINUTES);
		presentationInterrupt = false;
		int acc = 0;
		for (Presentation presentation : watched_presentations) {
			long watch = presentation.getStartTime().until(presentation.getEndTime(), ChronoUnit.SECONDS);
			acc += watch;
			if(presentation.getTerminatedBy().equals("User"))
				presentationInterrupt = true;
		}
		minutesWatched = acc/60;
		poi2stayTime = computePoiHoldingTime();
	}
	
	private Map<String,Long> computePoiHoldingTime() {
		Map<String,Long> poi2time = new HashMap<String, Long>();
		
		for (Position position : getVisited_poi()) {
			String name = position.getPoi_name();
			Long timeSpent = (position.getStart_time().until(position.getEnd_time(), ChronoUnit.SECONDS));
			Long acc = new Long(0);
			if(poi2time.containsKey(name))
				acc = poi2time.get(name);
			acc = acc + timeSpent;
			poi2time.put(name, acc);
			
		}
		
		//SORT IN ORDINE DECRESCENTE
		Map<String, Long> sorted = poi2time
		        .entrySet()
		        .stream()
		        .sorted(Map.Entry.<String,Long>comparingByValue().reversed())
		        .collect(
		            Collectors.toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2,
		                LinkedHashMap::new));
		
		
		return sorted;
	}

	public List<Position> getVisited_poi() {
		return visited_poi;
	}

	public List<Visitor> getVisitors() {
		return visitors;
	}

	public List<Presentation> getWatched_presentations() {
		return watched_presentations;
	}

	public Boolean getPresentationInterrupt() {
		return presentationInterrupt;
	}

	public Long getMinutesSpent() {
		return minutesSpent;
	}

	public Integer getMinutesWatched() {
		return minutesWatched;
	}

	public Boolean getWatchedMoreThanAvg() {
		return watchedMoreThanAvg;
	}

	public Boolean getStayedMoreThanAvg() {
		return stayedMoreThanAvg;
	}

	public Integer getSentimentPoints() {
		return sentimentPoints;
	}

	@Override
	public String toString() {
		return "Summary [visited_poi=" + visited_poi.size() + ", watched_presentations=" + watched_presentations.size()
				+ ", minutesSpent=" + minutesSpent + ", minutesWatched=" + minutesWatched + ", presentationInterrupt="
				+ presentationInterrupt + ", watchedMoreThanAvg=" + watchedMoreThanAvg + ", stayedMoreThanAvg="
				+ stayedMoreThanAvg + ", sentimentPoints=" + sentimentPoints + "]";
	}

	public Map<String,Long> getPoi2stayTime() {
		return poi2stayTime;
	}
	
	
	
	
}
