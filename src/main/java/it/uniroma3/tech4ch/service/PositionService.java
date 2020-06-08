package it.uniroma3.tech4ch.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.tech4ch.model.Position;
import it.uniroma3.tech4ch.repository.PositionRepository;

@Service
public class PositionService {

	@Autowired
	PositionRepository positionRepository;
	
	@Transactional
	public List<Position> getAll() {
		return positionRepository.findAll();
	}
	
	@Transactional
	public Position getById(Integer id) {
		return positionRepository.findById(id).get();
	}
	
	@Transactional
	public List<Position> getByVisitorId(Integer id_visitor) {
		return positionRepository.findByVisitorId(id_visitor);
	}
}
