package it.uniroma3.tech4ch.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.uniroma3.tech4ch.charts.ChartDataPoint;
import it.uniroma3.tech4ch.model.Statistics;
import it.uniroma3.tech4ch.model.Summary;
import it.uniroma3.tech4ch.model.Visitor;
import it.uniroma3.tech4ch.service.PositionService;
import it.uniroma3.tech4ch.service.PresentationService;
import it.uniroma3.tech4ch.service.VisitorService;

@Controller
public class VisitorController {

	@Autowired
	VisitorService visitorService;

	@Autowired
	PositionService positionService;

	@Autowired
	PresentationService presentationService;

	@Autowired
	public Statistics statistiche;


	@RequestMapping(value = "/visitor/{id}", method = RequestMethod.GET)
	public @ResponseBody Visitor getAllUsers(@PathVariable Integer id) {
		return visitorService.getById(id);
	}

	@PostMapping(value = "/summary")
	public String getVisitorSummary(@ModelAttribute Visitor visitor, ModelMap model){
		if(visitor.getId() == null && visitor.getGroup() == null) {
			return "summaryNotFoundError.html";
		}
		else if (visitor.getId()!=null) {
			try {
				visitor = visitorService.getById(visitor.getId());
			} catch (Exception e) {
				return "summaryNotFoundError.html";
			}
			Summary summary = new Summary(visitor, statistiche, visitorService, positionService, presentationService);
			Map<String,Long> poi2stay = summary.getPoi2stayTime();

			List<ChartDataPoint> poi2staydp = new ArrayList<>();
			for (String key : poi2stay.keySet()) {
				ChartDataPoint p = new ChartDataPoint();
				p.setX(key);
				p.setY(poi2stay.get(key).doubleValue());
				poi2staydp.add(p);
			}

			List<ChartDataPoint> avgStaydp = new ArrayList<>();

			ChartDataPoint p = new ChartDataPoint();
			p.setX("Visitor Stay Time");
			p.setY(summary.getMinutesSpent().doubleValue());
			avgStaydp.add(p);
			p = new ChartDataPoint();
			p.setX("Average Stay Time");
			p.setY(statistiche.getAverageIndividualStay());
			avgStaydp.add(p);

			List<ChartDataPoint> avgWatchdp = new ArrayList<>();
			p = new ChartDataPoint();
			p.setX("Visitor Watch Time");
			p.setY(summary.getMinutesWatched().doubleValue());
			avgWatchdp.add(p);
			p = new ChartDataPoint();
			p.setX("Average Watch Time");
			p.setY(statistiche.getAverageWatchTime()/60);
			avgWatchdp.add(p);


			if(poi2staydp.size()<10)
				model.addAttribute("stayData", poi2staydp);
			else
				model.addAttribute("stayData", poi2staydp.subList(0, 10));
			model.addAttribute("stayVS", avgStaydp);
			model.addAttribute("watchVS", avgWatchdp);

			model.addAttribute("titleStay", "Top 10 Point of Interest in terms of Time Stayed");
			model.addAttribute("titleVS", "Visitor Stay Time Comparison");
			model.addAttribute("titleVSW", "Visitor Presentation Watch Time Comparison");

			model.addAttribute("summary", summary);
			return "summary.html";
		}
		else {
			List<Visitor> visitors;
			try {
				visitors = visitorService.getGroup(visitor.getGroup());
				
				if(visitors.isEmpty())
					return "summaryNotFoundError.html";
			} catch (Exception e) {
				return "summaryNotFoundError.html";
			}
			Summary summary = new Summary(visitors, statistiche, visitorService, positionService, presentationService);
			Map<String,Long> poi2stay = summary.getPoi2stayTime();

			List<ChartDataPoint> poi2staydp = new ArrayList<>();
			for (String key : poi2stay.keySet()) {
				ChartDataPoint p = new ChartDataPoint();
				p.setX(key);
				p.setY(poi2stay.get(key).doubleValue());
				poi2staydp.add(p);
			}

			List<ChartDataPoint> avgStaydp = new ArrayList<>();

			ChartDataPoint p = new ChartDataPoint();
			p.setX("Visitor Stay Time");
			p.setY(summary.getMinutesSpent().doubleValue());
			avgStaydp.add(p);
			p = new ChartDataPoint();
			p.setX("Average Stay Time");
			p.setY(statistiche.getAverageIndividualStay());
			avgStaydp.add(p);

			List<ChartDataPoint> avgWatchdp = new ArrayList<>();
			p = new ChartDataPoint();
			p.setX("Visitor Watch Time");
			p.setY(summary.getMinutesWatched().doubleValue());
			avgWatchdp.add(p);
			p = new ChartDataPoint();
			p.setX("Average Watch Time");
			p.setY(statistiche.getAverageWatchTime());
			avgWatchdp.add(p);


			if(poi2staydp.size()<10)
				model.addAttribute("stayData", poi2staydp);
			else
				model.addAttribute("stayData", poi2staydp.subList(0, 10));
			model.addAttribute("stayVS", avgStaydp);
			model.addAttribute("watchVS", avgWatchdp);

			model.addAttribute("titleStay", "Top 10 Point of Interest in terms of Time Stayed");
			model.addAttribute("titleVS", "Visitor Stay Time Comparison");
			model.addAttribute("titleVSW", "Visitor Presentation Watch Time Comparison");

			model.addAttribute("summary", summary);
			return "summary.html";
		}


	}
}

