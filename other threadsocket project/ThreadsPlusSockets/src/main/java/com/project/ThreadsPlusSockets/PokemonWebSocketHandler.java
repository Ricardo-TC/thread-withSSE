package com.project.ThreadsPlusSockets;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class PokemonWebSocketHandler extends TextWebSocketHandler{
	
	private final Set<WebSocketSession> sessions = new HashSet<>();
	private static Logger logger = LoggerFactory.getLogger(PokemonWebSocketHandler.class);
	
	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message)throws IOException{
		
	}
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) {
		sessions.add(session);
	}
	
	public void sendStatusMessage(String statusMessage) throws IOException{
		logger.info("inside sendStatusMessage method");
		TextMessage message = new TextMessage(statusMessage);
		for(WebSocketSession session : sessions) {
			logger.info("inside for cycle");
			session.sendMessage(message);
		}
	}

}
