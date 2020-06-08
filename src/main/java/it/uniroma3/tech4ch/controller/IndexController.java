package it.uniroma3.tech4ch.controller;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.tech4ch.charts.ChartDataPoint;
import it.uniroma3.tech4ch.charts.ChartDataPoint3D;
import it.uniroma3.tech4ch.model.Statistics;
import it.uniroma3.tech4ch.model.Visitor;
import it.uniroma3.tech4ch.service.PositionService;
import it.uniroma3.tech4ch.service.PresentationService;
import it.uniroma3.tech4ch.service.VisitorService;

@Controller
public class IndexController {

	@Autowired
	VisitorService visitorService;
	
	@Autowired
	PositionService positionService;
	
	@Autowired
	PresentationService presentationService;
	
	@Autowired
	public Statistics statistiche;

	@RequestMapping(method = RequestMethod.GET)
	public String springMVC(ModelMap modelMap) {
		return "index.html";
	}
	

	
	@GetMapping(value = "/summaryForm")
	public String getVisitorSummary(ModelMap model){
		Visitor i = new Visitor();
		model.addAttribute("visitor", i);
		return "searchSummaryForm.html";
	}
	
	private List<ChartDataPoint3D> createBubbleChart() {
		Map<String, TreeMap<Integer, Integer>> roomXhourXvis = statistiche.getVisitorsPerRoomPerHour();
		List<ChartDataPoint3D> bubbleChartDataPoints = new ArrayList<>();
		
		for(String room : roomXhourXvis.keySet()) {
			TreeMap<Integer, Integer> hourXvis = roomXhourXvis.get(room);
			for(Integer hour: hourXvis.keySet()) {
				ChartDataPoint3D point = new ChartDataPoint3D();
				point.setX(room);
				point.setY(hourXvis.get(hour));
				point.setZ(hour.toString());
				bubbleChartDataPoints.add(point);
			}
		}
		return bubbleChartDataPoints;
	}
	
	private List<ChartDataPoint> createPieChart() {
		Map<String, Double> positionAttractionPower = statistiche.getPositionAttractionPower();
		List<ChartDataPoint> pieChartDataPoints = new ArrayList<>();
		for (String key : positionAttractionPower.keySet()) {
			ChartDataPoint p2 = new ChartDataPoint();
			p2.setX(key);
			p2.setY(positionAttractionPower.get(key)*100);
			pieChartDataPoints.add(p2);
		}
		pieChartDataPoints = pieChartDataPoints.subList(0, 10);
		Double oth = 100.;
		for (ChartDataPoint ponto2 : pieChartDataPoints) {
			oth -= ponto2.getY();
		}
		ChartDataPoint p2 = new ChartDataPoint();
		p2.setX("Others");
		p2.setY(oth);
		pieChartDataPoints.add(p2);
		return pieChartDataPoints;
	}
	
	@GetMapping(value = "/statistics")
	public String holdingPowerChart(ModelMap model){
		List<ChartDataPoint> barGraphDp = createBarGraph();
		List<ChartDataPoint> pieChartDataPoints = createPieChart();
		List<ChartDataPoint> splineChartDataPoints = createSplineChart();
		List<ChartDataPoint3D> bubbleChartDataPoints = createBubbleChart();

		Set<String> set = new HashSet<>();
		for(ChartDataPoint s : pieChartDataPoints) {
			set.add(s.getX());
		}
		set.remove("Others");
		
		Map<String, Integer> map = new HashMap<>();
		int count = 1;
		for(String s : set) {
			map.put(s, count);
			count++;
		}
		
		
		//AGGIUNGI SOLO TOP 10 
		bubbleChartDataPoints.removeIf(it -> !set.contains(it.getX()));

		for (ChartDataPoint3D ds : bubbleChartDataPoints) {
			ds.setX(map.get(ds.getX()).toString());
		}
		
		Double stay = statistiche.getAverageIndividualStay();
		double time = statistiche.getAverageWatchTime()/60;
		DecimalFormat numberFormat = new DecimalFormat("#.00");
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");  
		String maxDate = dateFormat.format(statistiche.getMaxDate());  
		String minDate = dateFormat.format(statistiche.getMinDate());  
		
		model.addAttribute("maxDate",maxDate);
		model.addAttribute("minDate",minDate);
		model.addAttribute("splineDp", splineChartDataPoints);
		model.addAttribute("barDp", barGraphDp);
		model.addAttribute("pieDp", pieChartDataPoints);
		model.addAttribute("bubbleDp", bubbleChartDataPoints);
		model.addAttribute("avgStay",numberFormat.format(stay));
		model.addAttribute("avgPresW",statistiche.getaveragePresentationsWatched().intValue());
		model.addAttribute("avgPresT",numberFormat.format(time));
		
		return "museumStatistics.html";
	}

	private List<ChartDataPoint> createSplineChart() {
		Map<Integer, Integer> visXhour = statistiche.getVisitorsPerHour();
		List<ChartDataPoint> splineChartDataPoints = new ArrayList<>();
		for (Integer key : visXhour.keySet()) {
			ChartDataPoint p3 = new ChartDataPoint();
			Integer next = new Integer(key+1);
			p3.setX(key.toString() + ":00 - " + next.toString()+":00");
			p3.setY(visXhour.get(key).doubleValue());
			splineChartDataPoints.add(p3);
		}
		return splineChartDataPoints;
	}

	private List<ChartDataPoint> createBarGraph() {
		Map<String, Long> holdingPower = statistiche.getPositionStayTime();
		List<ChartDataPoint> dp1 = new ArrayList<>();
		for (String key : holdingPower.keySet()) {
			ChartDataPoint p = new ChartDataPoint();
			p.setX(key);
			p.setY(holdingPower.get(key).doubleValue());
			dp1.add(p);
		}
		List<ChartDataPoint> barGraphDp = dp1.subList(0, 10);
		return barGraphDp;
	}

}