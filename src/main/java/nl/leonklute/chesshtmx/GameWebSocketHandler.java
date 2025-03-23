package nl.leonklute.chesshtmx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GameWebSocketHandler implements WebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(GameWebSocketHandler.class);

    private final Map<Principal, WebSocketSession> webSocketSessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        webSocketSessions.put(session.getPrincipal(), session);
        log.info("ws connection: {}", session);
        log.debug("webSocketSessions: {}", webSocketSessions);
    }

    @Override
    public void handleMessage(@NonNull WebSocketSession session, @NonNull WebSocketMessage<?> message) throws Exception {
        log.error("Got message: '{}' from session: {}", message, session);
    }

    @Override
    public void handleTransportError(@NonNull WebSocketSession session, @NonNull Throwable exception) throws Exception {
        log.error("Got transportError on: '{}'", session, exception);
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus closeStatus) throws Exception {
        log.info("Session closed with status: {}", closeStatus);
        webSocketSessions.remove(session.getPrincipal(), session);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    public  void send(Principal principal, TextMessage message) throws IOException {
        var session = webSocketSessions.get(principal);
        if (session != null && session.isOpen()){
            log.info("Send message: {}", message);
            synchronized (session) {
                session.sendMessage(message);
            }
        }
    }
}
