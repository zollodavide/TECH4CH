package it.uniroma3.tech4ch.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.tech4ch.model.Visitor;
import it.uniroma3.tech4ch.repository.VisitorRepository;

@Service
public class VisitorService {

	@Autowired
	VisitorRepository visitorRepository;
	
	@Transactional
	public List<Visitor> getAllVisitors() {
		return visitorRepository.findAll();
	}
	
	
	
	@Transactional
	public Visitor getById(Integer id) {
		return visitorRepository.findById(id).get();
	}
	
	@Transactional
	public List<Visitor> getGroup(Integer group_id){
		return visitorRepository.findByGroup(group_id);
	}
	
	@Transactional
	public boolean addVisitor(Visitor visitor) {
		return visitorRepository.save(visitor) != null;
	}
	
}
