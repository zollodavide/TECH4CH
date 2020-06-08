package it.uniroma3.tech4ch.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.tech4ch.model.Position;

public interface PositionRepository extends CrudRepository<Position, Integer>{
	
	@Override
	public List<Position> findAll();
	
	@Override
	public Optional<Position> findById(Integer id);
	
	public List<Position> findByVisitorId(Integer id_visitor);

}
