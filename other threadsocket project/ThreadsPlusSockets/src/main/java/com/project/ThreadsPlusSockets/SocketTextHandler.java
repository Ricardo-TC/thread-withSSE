package com.project.ThreadsPlusSockets;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.project.ThreadsPlusSockets.service.PokemonService;

@Component
public class SocketTextHandler extends TextWebSocketHandler{

//	private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
	private final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
//	private final Set<WebSocketSession> sessions = new HashSet<>();
	private static Logger logger = LoggerFactory.getLogger(SocketTextHandler.class);
	private final Lock lock = new ReentrantLock();
	private final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();
	private PokemonService pokemonService;
	private String serviceMessage;
	private WebSocketSession generalSession;
	
	public SocketTextHandler(PokemonService pokemonService) {
		super();
		this.pokemonService = pokemonService;
	}

	public SocketTextHandler() {
		super();
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//		sessions.remove(session);
//		logger.info("=====connection closed");
//		logger.info("SKTSESSION"+sessions);
		super.afterConnectionClosed(session, status);
	}
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws IOException, Exception {
		logger.info("*****afterConnectionEstablished");
		sessions.add(session);
		super.afterConnectionEstablished(session);
		this.generalSession = session;
		logger.info("method afterConnectionEstablished:"+generalSession.getId());
	}
	
	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) throws InterruptedException, IOException{
		logger.info("*****handleTextMessage");
		logger.info("Sessions:"+sessions);
		logger.info("Session:"+session);
		String payload = message.getPayload();
		JSONObject jsonObject = new JSONObject(payload);
//		session.sendMessage(new TextMessage("Hi " + jsonObject.get("user") + " how may we help you?"));
		logger.info("serviceMessage just test:"+serviceMessage);
		session.sendMessage(new TextMessage(jsonObject.get("user") + serviceMessage));
	}
	
	
	public synchronized void sendClientMessage(String message) throws IOException {
		logger.info("*****sendClientMessage");
		serviceMessage = message;
		logger.info("--On method sendClientMessage, current msg: "+serviceMessage);
		logger.info("--On method sendClientMessage, generalSession: "+generalSession);
		if(generalSession != null && generalSession.isOpen()) {
			logger.info("--On method sendClientMessage, inside IF");
			generalSession.sendMessage(new TextMessage(message));
		}
		
//		sessions.add(generalSession);
		logger.info("Session"+sessions);
//		generalSession.sendMessage(new TextMessage(serviceMessage));
	}

//	@Override
//	public void handleTextMessage(WebSocketSession session, TextMessage message) throws InterruptedException, IOException{
//		logger.debug("=====connection handleTextMessage");
//		String payload = message.getPayload();
////		this.pokemonService.processWebSocketMessage(session, message);
//		JSONObject jsonObject = new JSONObject(payload);
//		session.sendMessage(new TextMessage(jsonObject.getString(payload)));
//		logger.debug("=====connection handleTextMessage2");
//	}
	
//	original code
//	@Override
//	public void handleTextMessage(WebSocketSession session, TextMessage message) throws InterruptedException, IOException{
//		String payload = message.getPayload();
//		JSONObject jsonObject = new JSONObject(payload);
//		session.sendMessage(new TextMessage("Hi " + jsonObject.get("user") + " how may we help you?"));
//	}
	
	
//	@Override
//	public void afterConnectionEstablished(WebSocketSession session) throws IOException {
//		sessions.add(session);
////		session.sendMessage(new TextMessage("Msg Test mode from method after connection.."));
//		logger.info("=====connection succecced");
//		logger.info("SKTSESSION"+sessions);
//	}
//	
//	@Override
//	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
//		sessions.remove(session);
//		logger.info("=====connection closed");
//		logger.info("SKTSESSION"+sessions);
//	}
	
//	public void sendMessageToClient(String message) throws IOException{
//		logger.info("----*----on sockethandler sendmsg method");
//		messageQueue.offer(message);
//	}
//	
//	public void processMessageQueue() throws IOException {
//		String message;
//		while((message = messageQueue.poll()) != null) {
////			lock.lock();
//			try {
//				logger.info("SKTSESSION before for:"+sessions);
//				for(WebSocketSession session : sessions) {
//					logger.info("----*----on sockethandler 'for'");
//					if(session.isOpen()) {
//						session.sendMessage(new TextMessage(message));
//						logger.info("----*----on sockethandler after if");
//					}
//				}
//			}finally{
////				lock.unlock();
//			}
//		}
//	}
	
}
