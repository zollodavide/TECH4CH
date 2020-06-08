package it.uniroma3.tech4ch.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.tech4ch.model.Presentation;
import it.uniroma3.tech4ch.repository.PresentationRepository;

@Service
public class PresentationService {

	@Autowired
	PresentationRepository presentationRespository;
	
	@Transactional
	public List<Presentation> getAllPresentations() {
		return presentationRespository.findAll();
	}
	
	@Transactional
	public Presentation getById(Integer id) {
		return presentationRespository.findById(id).get();
	}
	
	@Transactional
	public List<Presentation> getByVisitorId(Integer id_visitor) {
		return presentationRespository.findByVisitorId(id_visitor);
	}
}