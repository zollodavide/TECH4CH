package it.uniroma3.tech4ch.controller;

import java.time.temporal.ChronoUnit;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import it.uniroma3.tech4ch.model.Position;
import it.uniroma3.tech4ch.model.Statistics;
import it.uniroma3.tech4ch.model.Visitor;
import it.uniroma3.tech4ch.service.PositionService;
import it.uniroma3.tech4ch.service.PresentationService;
import it.uniroma3.tech4ch.service.VisitorService;

@Controller
public class VisitController {

	@Autowired
	VisitorService visitorService;
	
	@Autowired
	PositionService positionService;
	
	@Autowired
	PresentationService presentationService;
	
	@Autowired
	public Statistics statistiche;
	
	@GetMapping(value = "/newVisit")
	public String getVisitorSummary(ModelMap model){
		Visitor i = new Visitor();
		model.addAttribute("visitor", i);
		return "searchVisit.html";
	}
	
	@GetMapping(value = "/visitEnd")
	public String endVisit(ModelMap model, HttpServletRequest request){
		request.getSession().removeAttribute("positions");
		return "visitEnd.html";
	}
	
	@PostMapping(value = "/replayVisit")
	public String replayVisit(@ModelAttribute Visitor visitor, ModelMap model, HttpServletRequest request){
		
		if(visitor.getId() == null && visitor.getGroup() == null) {
			return "summaryNotFoundError.html";
		}
		else if (visitor.getId()!=null) {
			try {
				visitor = visitorService.getById(visitor.getId());
				
			} catch (Exception e) {
				return "summaryNotFoundError.html";
			}
			Integer id = visitor.getId();
			List<Position> positions = positionService.getByVisitorId(id);
			if(positions.size()==0)
				return "summaryNotFoundError.html";

			request.getSession().setAttribute("positions", positions);
			model.addAttribute("title", "Visitor " + visitor.getId());
			model.addAttribute("positions", positions);
			model.addAttribute("nextPosition", positions.get(0));
			return "playVisit.html";
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
			model.addAttribute("title", "Group " + visitors.get(0).getGroup());
			List<Position> positions = positionService.getByVisitorId(visitors.get(0).getId());
			if(positions.size()==0)
				return "summaryNotFoundError.html";

			request.getSession().setAttribute("positions", positions);
			model.addAttribute("visitor", visitor);
			model.addAttribute("positions", positions);
			model.addAttribute("nextPosition", positions.get(0));
			return "playVisit.html";
		}
	}
	
	@GetMapping(value = "/visit/{number}")
	public String visitPoi(@PathVariable Integer number, ModelMap model, HttpServletRequest request){
		@SuppressWarnings("unchecked")
		List<Position> positions = (List<Position>) request.getSession().getAttribute("positions");
		Position current = positions.get(number);
		if(number == 0)
			model.addAttribute("first", true);
		else 
			model.addAttribute("first", false);
		
		if(number >= positions.size()-1) 
			model.addAttribute("last", true);
		else 
			model.addAttribute("last", false);

		Integer next = number+1;
		Integer back = number-1;
		model.addAttribute("index", number);
		model.addAttribute("next", next);
		model.addAttribute("back", back	);
		model.addAttribute("currentPosition", current);
		model.addAttribute("positions", positions);
		model.addAttribute("totalTime", current.getStart_time().until(current.getEnd_time(), ChronoUnit.SECONDS));

		return "visit.html";
	}
}
