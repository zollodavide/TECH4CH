package it.uniroma3.tech4ch.model;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import it.uniroma3.tech4ch.service.PositionService;
import it.uniroma3.tech4ch.service.PresentationService;
import it.uniroma3.tech4ch.service.VisitorService;

@Component
public class Statistics {
	
	VisitorService visitorService;
	PresentationService presentationService;
	PositionService positionService;
	
	private Date minDate;
	private Date maxDate;
	private Double averageGroupStay;
	private Double averageIndividualStay;
	private Double averagePresentationsWatched;
	private Double averageWatchTime;

	private Map<String, TreeMap<Integer, Integer>> visitorsPerRoomPerHour;
	private Map<Integer, Integer> visitorsPerHour; 
	private Map<String,Double> positionAttractionPower;
	private Map<String,Long> positionStayTime;
	
	@SuppressWarnings("deprecation")
	public Statistics(VisitorService visitorService, PresentationService presentationService, PositionService positionService) {
		this.visitorService = visitorService;
		this.presentationService = presentationService;
		this.positionService = positionService;
		this.minDate = new Date(2011-1900,3-1,17);
		this.maxDate = new Date(2012-1900,2-1,14);
	
		visitorsPerHour = visitorPerHours();
		averageIndividualStay = averageVisitorStay();
		averageGroupStay = averageGroupStay();
		averagePresentationsWatched = averagePresentationsWatched();
		averageWatchTime = averageWatchedTime();
		positionAttractionPower = poiAttractionPower();
		positionStayTime = averagePoiHoldingTime();
		visitorsPerRoomPerHour = visitorsPerRoomPerHour();
	}
	

	public Map<Integer, Integer> getVisitorsPerHour() {
		return visitorsPerHour;
	}

	public Map<String, TreeMap<Integer, Integer>> getVisitorsPerRoomPerHour() {
		return visitorsPerRoomPerHour;
	}

	public Double getAverageGroupStay() {
		return averageGroupStay;
	}

	public Double getaveragePresentationsWatched() {
		return averagePresentationsWatched;
	}

	public Double getAverageWatchTime() {
		return averageWatchTime;
	}


	public Map<String, Double> getPositionAttractionPower() {
		return positionAttractionPower;
	}
	
	public Double getAverageIndividualStay() {
		return averageIndividualStay;
	}

	public Map<String, Long> getPositionStayTime() {
		return positionStayTime;
	}
	
	public Date getMinDate() {
		return minDate;
	}


	public Date getMaxDate() {
		return maxDate;
	}


	private Map<Integer, Integer> visitorPerHours() {
		List<Visitor> visitors = visitorService.getAllVisitors();
		Map<Integer, Integer> hour2visitors = new TreeMap<Integer, Integer>();
		
		for (Visitor visitor : visitors) {
			Integer start = visitor.getStartTime().getHour();
			Integer end = visitor.getEndTime().getHour();
			Integer count = 0;
			if (end == start) {
				if(hour2visitors.containsKey(start))
					count = hour2visitors.get(start);
				count++;
				hour2visitors.put(start, count);
			} 
			else {
				for(int i = start; i<=end; i++) {
					if(hour2visitors.containsKey(i))
						count = hour2visitors.get(i);
					count++;
					hour2visitors.put(i, count);
				}
			}
		}
		return hour2visitors;
	}
	
	private Map<String, TreeMap<Integer, Integer>> visitorsPerRoomPerHour(){
		List<Position> positions = positionService.getAll();
		Map<String, TreeMap<Integer, Integer>> room2hour2visitors = new HashMap<String, TreeMap<Integer,Integer>>();
		
		for (Position position : positions) {
			String poiName = position.getPoi_name();
			TreeMap<Integer,Integer> value = new TreeMap<Integer,Integer>();
			if(room2hour2visitors.containsKey(poiName))
				value = room2hour2visitors.get(poiName);
			
			Integer start = position.getStart_time().getHour();
			Integer end = position.getEnd_time().getHour();
			Integer count = 0;
			if(end == start) {
				if(value.containsKey(start))
					count = value.get(start);
				count++;
				value.put(start, count);
			}
			else {
				for(int i = start; i<=end; i++) {
					if(value.containsKey(i))
						count = value.get(i);
					count++;
					value.put(i, count);
				}
			}
			room2hour2visitors.put(poiName, value);
		}
		return room2hour2visitors;
	}
	

	private Double averageVisitorStay() {
		List<Visitor> visitors = visitorService.getAllVisitors();
		Double accTime = 0.;
		Integer count = visitors.size();
		for (Visitor visitor : visitors) {
			accTime += visitor.getStartTime().until(visitor.getEndTime(), ChronoUnit.MINUTES);
		}
		accTime = accTime/count;
		return accTime;
	}
	
	private Double averageGroupStay() {
		List<Visitor> visitors = visitorService.getAllVisitors();
		Map<Integer, Visitor> map = new HashMap<Integer, Visitor>();
		for (Visitor visitor : visitors) {
			map.put(visitor.getGroup(), visitor);
		}
		Double accTime = 0.;
		Integer count = map.size();
		for(Visitor visitor : map.values()) {
			accTime += visitor.getStartTime().until(visitor.getEndTime(), ChronoUnit.MINUTES);
		}
		accTime = accTime/count;
		return accTime;
	}
	
	private Double averagePresentationsWatched() {
		List<Presentation> presentations = presentationService.getAllPresentations();
		Set<Integer> visitors = new HashSet<Integer>();
		for (Presentation presentation : presentations) {
			visitors.add(presentation.getVisitorId());
		}
		Double avg = (double) (presentations.size()/visitors.size());			
		return avg;
	}
	
	private Double averageWatchedTime() {
		List<Presentation> presentations = presentationService.getAllPresentations();
		Set<Integer> visitors = new HashSet<Integer>();
		Double acc = 0.;
		for (Presentation presentation : presentations) {
			visitors.add(presentation.getVisitorId());
			acc += presentation.getStartTime().until(presentation.getEndTime(), ChronoUnit.SECONDS);
		}
		
		
		Double avg = (double) (acc/visitors.size());			
		return avg;
		
	}
	
	private Map<String,Double> poiAttractionPower() {
		List<Position> positions = positionService.getAll();
		Set<Integer> vis = new HashSet<Integer>();
		Map<String,Double> poi2attraction = new HashMap<String, Double>();
		
		for (Position position : positions) {
			String name = position.getPoi_name();
			vis.add(position.getVisitor_id());
			Double acc = 0.;
			if(poi2attraction.containsKey(name))
				acc = poi2attraction.get(name);
			acc = acc+1;
			poi2attraction.put(name, acc);
		}
		Integer totalVisitors = positions.size();
		
		for(String name : poi2attraction.keySet()) {
			Double attraction = poi2attraction.get(name);
			attraction = attraction / totalVisitors;
			poi2attraction.put(name, attraction);
		}
		
		//SORT IN ORDINE DECRESCENTE
		Map<String, Double> sorted = poi2attraction
		        .entrySet()
		        .stream()
		        .sorted(Map.Entry.<String,Double>comparingByValue().reversed())
		        .collect(
		            Collectors.toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2,
		                LinkedHashMap::new));

		return sorted;
	}
	
	private Map<String,Long> averagePoiHoldingTime() {
		List<Position> positions = positionService.getAll();
		Map<String,Long> poi2time = new HashMap<String, Long>();
		Map<String,Integer> poi2count = new HashMap<String, Integer>();

		for (Position position : positions) {
			String name = position.getPoi_name();
			Long timeSpent = (position.getStart_time().until(position.getEnd_time(), ChronoUnit.SECONDS));
			Long acc = new Long(0);
			if(poi2time.containsKey(name))
				acc = poi2time.get(name);
			acc = acc + timeSpent;
			poi2time.put(name, acc);
			
			Integer count = 0;
			if(poi2count.containsKey(name))
				count = poi2count.get(name);
			count = count + 1;
			poi2count.put(name, count);
		}
		
		for(String key : poi2time.keySet()) {
			Long time = poi2time.get(key);
			time = time / poi2count.get(key);
			poi2time.put(key, time);
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

}