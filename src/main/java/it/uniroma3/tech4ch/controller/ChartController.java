package it.uniroma3.tech4ch.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import it.uniroma3.tech4ch.charts.ChartDataPoint;
import it.uniroma3.tech4ch.model.Statistics;
import it.uniroma3.tech4ch.service.PositionService;
import it.uniroma3.tech4ch.service.PresentationService;
import it.uniroma3.tech4ch.service.VisitorService;

@Controller
public class ChartController {

	@Autowired
	VisitorService visitorService;
	
	@Autowired
	PositionService positionService;
	
	@Autowired
	PresentationService presentationService;
	
	@Autowired
	public Statistics statistiche;

	
	@GetMapping(value = "/pieChart")
	public String createTop10PoiAttractionChart(ModelMap model){
		Map<String, Double> positionAttractionPower = statistiche.getPositionAttractionPower();
		List<ChartDataPoint> dp = new ArrayList<>();
		for (String key : positionAttractionPower.keySet()) {
			ChartDataPoint p = new ChartDataPoint();
			p.setX(key);
			p.setY(positionAttractionPower.get(key)*100);
			dp.add(p);
		}
		ChartDataPoint p = new ChartDataPoint();
		List<ChartDataPoint> subList = dp.subList(0, 10);
		Double oth = 100.;
		for (ChartDataPoint ponto2 : subList) {
			oth -= ponto2.getY();
		}
		p.setX("Others");
		p.setY(oth);
		subList.add(p);
		model.addAttribute("title", "Top 10 Points Of Interests in terms of visits");
		model.addAttribute("pontos", subList);
		return "pieChart.html";
	}
	
	
	@GetMapping(value = "/barChart")
	public String holdingPowerChart(ModelMap model){
		Map<String, Long> holdingPower = statistiche.getPositionStayTime();
		List<ChartDataPoint> dp = new ArrayList<>();
		for (String key : holdingPower.keySet()) {
			ChartDataPoint p = new ChartDataPoint();
			p.setX(key);
			p.setY(holdingPower.get(key).doubleValue());
			dp.add(p);
		}
		List<ChartDataPoint> subList = dp.subList(0, 10);
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");  
        String maxDate = dateFormat.format(statistiche.getMaxDate());  
        String minDate = dateFormat.format(statistiche.getMinDate());  
        model.addAttribute("maxDate",maxDate);
		model.addAttribute("minDate",minDate);
		model.addAttribute("title", "Top 10 Points Of Interests in terms of Average Stay Time");
		model.addAttribute("pontos", subList);
		return "barChart.html";
	}
	
	
	@GetMapping(value = "/splineChart")
	public String visitorsPerHourChart(ModelMap model){
		Map<Integer, Integer> visXhour = statistiche.getVisitorsPerHour();
		List<ChartDataPoint> dp = new ArrayList<>();
		for (Integer key : visXhour.keySet()) {
			ChartDataPoint p = new ChartDataPoint();
			Integer next = new Integer(key+1);
			p.setX(key.toString() + ":00 - " + next.toString()+":00");
			p.setY(visXhour.get(key).doubleValue());
			dp.add(p);
		}
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");  
        String maxDate = dateFormat.format(statistiche.getMaxDate());  
        String minDate = dateFormat.format(statistiche.getMinDate());  
        model.addAttribute("maxDate",maxDate);
		model.addAttribute("minDate",minDate);
		model.addAttribute("title", "Average Visitors per Hour");
		model.addAttribute("pontos", dp);
		return "splineChart.html";
	}
}
