package net.froelund.drawing.control;

import net.froelund.Cache;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.inject.Inject;
import javax.websocket.Session;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles the session communication.
 *
 * Created by froelund on 5/30/14.
 */
@Stateless
public class SessionService {

    public static final String SESSION_INDEX = "sessionIndex";
    @Inject
    Cache cache;

    private static final Logger logger = Logger.getLogger(SessionService.class.getName());

    @PostConstruct
    public void init(){
        if (!cache.has("sessionIndex")){
            cache.put(SESSION_INDEX, Collections.synchronizedList(new ArrayList<Session>()));
        }
    }

    public void clientConnect(Session session){
        getSessionIndex().add(session);
        logger.log(Level.INFO, "Client connected. Total {0}", getSessionCount());
    }
    @Asynchronous
    public void sendObject(Session session, Object o){
        if (session.isOpen()){
            session.getAsyncRemote().sendObject(o);
            logger.log(Level.INFO, "Object send.");
        }
    }
    public void clienDisconnect(Session session){
        try {
            getSessionIndex().remove(session);
        }catch (NullPointerException e){
            logger.log(Level.INFO, "Tried to remove session, but it was already gone..");
        }
        logger.log(Level.INFO, "Client disconnected. Total {0}", getSessionCount());
    }
    public void broadcastObject(Session sendingSession, Object o){
        for (Session session : sendingSession.getOpenSessions()) {
            if (!sendingSession.equals(session)){
                sendObject(session, o);
            }
        }
    }
    public int getSessionCount(){
        return getSessionIndex().size();
    }

    List<Session> getSessionIndex(){
        return (List<Session>) cache.get("sessionIndex");
    }
}
