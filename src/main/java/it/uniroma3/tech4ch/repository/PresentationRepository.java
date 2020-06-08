package it.uniroma3.tech4ch.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.tech4ch.model.Presentation;

public interface PresentationRepository extends CrudRepository<Presentation, Integer>{
	
	 
	@Override
	public List<Presentation> findAll();
	
	@Override
	public Optional<Presentation> findById(Integer id);
	
	public List<Presentation> findByVisitorId(Integer id_visitor);


}
