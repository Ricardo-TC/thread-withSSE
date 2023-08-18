package com.project.ThreadsPlusSockets.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.project.ThreadsPlusSockets.entity.Pokemon;
import com.project.ThreadsPlusSockets.entity.repository.PokemonRepository;

@Service
public class PokemonService implements Runnable{
	
	private final PokemonRepository pokemonRepository;
	private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();
	private boolean updatesEnabled = true;
	private int startID;
	private int endID;
	
	private static Logger logger = LoggerFactory.getLogger(PokemonService.class);

	
	public PokemonService(PokemonRepository pokemonRepository) {
		super();
		this.pokemonRepository = pokemonRepository;
	}
	
	public List<Pokemon> getPokes(){
		return this.pokemonRepository.findAll();
	}
	
	public void emptyTable() {
		try {
			this.pokemonRepository.emptyTable();
		}catch(Exception e) {
			e.getMessage();
		}
	}
	
	public synchronized String SetDataFromPokeapi(int startID, int endID) throws IOException{
		this.startID = startID;
		this.endID = endID;
		if(startID>endID) {
			return "ID's are not valid, startID should be higher than endID. ";
		}else {
			Thread myThread = new Thread(this);
			myThread.start();
		}
		return "Task received, in progress... ";
	}

	@Override
	public void run() {
		try {
			String msg = "";
			int cont = 0;
			int sid = startID;
			int eid = endID;
			int total = (eid - sid) +1;
			Pokemon pokemon = new Pokemon();
			while(sid <= eid) {
				URL url = new URL("https://pokeapi.co/api/v2/pokemon/" + sid);
				URLConnection con = url.openConnection();
				InputStream is = con.getInputStream();
				JSONObject json;
				try(BufferedReader br = new BufferedReader(new InputStreamReader(is))){
					StringBuilder response = new StringBuilder();
					String line;
					while((line = br.readLine()) != null) {
						response.append(line);
					}
					json = new JSONObject(response.toString());
				}
				pokemon.setId(sid);
				pokemon.setName(json.getString("name"));
				pokemon.setLevel(json.getInt("base_experience"));
				JSONArray types = json.getJSONArray("types");
				pokemon.setType(types.getJSONObject(0).getJSONObject("type").getString("name"));
				this.pokemonRepository.save(pokemon);
				cont++;
				msg = "--Record: " + sid + ", from " + total + ", in " + Thread.currentThread()+"--";
				logger.info(msg);
				logger.info("sid:"+sid+" eid:"+eid);
				if(!updatesEnabled) {
					return;
				}
				sendMessageToClient(msg);
				Thread.sleep(500);
				sid++;
			}
			logger.info("--Task Finished, total records: " + cont + ", last Record: " + (sid-1) + ", from " + Thread.currentThread());
			logger.info("sid:"+sid+" eid:"+eid);
			sendMessageToClient("--Task Finished, total records: " + cont + ", last Record: " + (sid-1) + ", from " + Thread.currentThread());
		}catch(Exception e) {
			e.getMessage();
		}
	}
	
	public SseEmitter pokemonStatus() {
		SseEmitter emitter = new SseEmitter();
		emitters.add(emitter);
		emitter.onCompletion(()->emitters.remove(emitter));
		return emitter;
	}
	
	public void sendMessageToClient(String message) {
		emitters.forEach(emitter -> {
			try {
				emitter.send(SseEmitter.event().data(message));
			}catch(Exception e) {
				emitter.complete();
				emitters.remove(emitter);
			}
		});
	}
	
    public void enableUpdates() {
        updatesEnabled = true;
    }

    public void disableUpdates() {
        updatesEnabled = false;
    }
	
}