package net.froelund.drawing.control;

import net.froelund.Cache;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.inject.Inject;
import javax.websocket.Session;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles the session communication.
 *
 * Created by froelund on 5/30/14.
 */
@Stateless
public class SessionService {
    private static final Logger logger = Logger.getLogger(SessionService.class.getName());

    @Asynchronous
    public void sendObject(Session session, Object o){
        if (session.isOpen()){
            session.getAsyncRemote().sendObject(o);
            logger.log(Level.FINE, "Object send.");
        }
    }
    @Asynchronous
    public void broadcastObject(Session sendingSession, Object o){
        Set<Session> openSessions = sendingSession.getOpenSessions();
        logger.log(Level.INFO, "Broadcasting object to {0} sessions", openSessions.size());
        for (Session session : openSessions) {
            if (!sendingSession.equals(session)){
                sendObject(session, o);
            }
        }
    }
}
