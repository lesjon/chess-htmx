package nl.leonklute.chesshtmx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class GameWebSocketHandler implements WebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(GameWebSocketHandler.class);

    private final List<WebSocketSession> webSocketSessions = new CopyOnWriteArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        webSocketSessions.add(session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        log.error("Got message: '{}' from session: {}", message, session);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("Got transportError on: '{}'", session, exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        log.info("Session closed with status: {}", closeStatus);
        webSocketSessions.remove(session);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    public void broadcast(TextMessage message){
        webSocketSessions.stream().filter(WebSocketSession::isOpen).forEach(s -> {
            try {
                s.sendMessage(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
