package it.uniroma3.tech4ch.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.tech4ch.model.Visitor;

public interface VisitorRepository extends CrudRepository<Visitor, Integer>{

	@Override
	public List<Visitor> findAll();
	
	@Override
	public Optional<Visitor> findById(Integer id);
	
	public List<Visitor> findByGroup(Integer group);
	

}
