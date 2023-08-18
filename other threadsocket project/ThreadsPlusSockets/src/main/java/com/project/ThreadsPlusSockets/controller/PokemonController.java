package com.project.ThreadsPlusSockets.controller;

import java.io.IOException;
import java.util.List;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.project.ThreadsPlusSockets.entity.Pokemon;
import com.project.ThreadsPlusSockets.service.PokemonService;

@RestController
@RequestMapping("/Pokemon")
@CrossOrigin(origins = "http://localhost:8080")
public class PokemonController {
	
	@Autowired
	private final PokemonService pokemonService;
//	private static Logger logger = LoggerFactory.getLogger(PokemonController.class);
	public PokemonController(PokemonService pokemonService) {
		super();
		this.pokemonService = pokemonService;
	}
	
	@GetMapping("/pokemon-status")
	public SseEmitter pokemonStatus(){
		return pokemonService.pokemonStatus();
	}
	
	
	@GetMapping(path = "/getPokes", produces = "application/json")
	public List<Pokemon> getPokes(){
		return this.pokemonService.getPokes();
	}
	
	@DeleteMapping(path = "/emptyTable")
	public ResponseEntity<String> emptyTable(){
		this.pokemonService.emptyTable();
//		return ResponseEntity.ok("Table cleaned");
		return ResponseEntity.status(HttpStatus.OK).body("Table cleaned");
	}
	
	@PostMapping(path = "/setPokesByRank")
	public ResponseEntity<String> setDataFromPokeapi(@RequestParam int startID, @RequestParam int endID) throws IOException {
		return new ResponseEntity<String>(this.pokemonService.SetDataFromPokeapi(startID, endID), HttpStatus.ACCEPTED);
	}
	
    @PostMapping("/enable-updates")
    public void enableUpdates() {
        pokemonService.enableUpdates();
    }

    @PostMapping("/disable-updates")
    public void disableUpdates() {
        pokemonService.disableUpdates();
    }

}
