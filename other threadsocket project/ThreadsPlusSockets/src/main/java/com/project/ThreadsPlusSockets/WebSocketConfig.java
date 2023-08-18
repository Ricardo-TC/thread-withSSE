package com.project.ThreadsPlusSockets;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.project.ThreadsPlusSockets.service.PokemonService;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer{

	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(new SocketTextHandler(), "/user").setAllowedOrigins("*");
	}
	
//	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//		registry.addHandler(new PokemonWebSocketHandler(), "/pokemon-status").setAllowedOrigins("*");
//	}
	
//	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//		registry.addHandler(new PokemonService(), "/user").setAllowedOrigins("*");
//	}
//	
}
