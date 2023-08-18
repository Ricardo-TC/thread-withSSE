package com.project.ThreadsPlusSockets.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import com.project.ThreadsPlusSockets.entity.Pokemon;

@Repository
public interface PokemonRepository extends JpaRepository<Pokemon, Integer>{
	
	@Procedure(procedureName = "emptyTable")
	public void emptyTable();

}
