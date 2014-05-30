package net.froelund.drawing.control;

import javax.ejb.*;
import javax.websocket.Session;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by froelund on 5/30/14.
 */
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class SessionService {

    private static final Logger logger = Logger.getLogger(SessionService.class.getName());

    List<Session> sessionIndex = new ArrayList<>();

    public void clientConnect(Session session){
        sessionIndex.add(session);
        logger.log(Level.INFO, "Client connected. Total {0}", getSessionCount());
    }
    @Lock(LockType.READ)
    public void sendObject(Session session, Object o){
        if (session.isOpen()){
            session.getAsyncRemote().sendObject(o);
            logger.log(Level.INFO, "Object send.");
        }
    }
    public void clienDisconnect(Session session){
        try {
            sessionIndex.remove(session);
        }catch (NullPointerException e){
            logger.log(Level.INFO, "Tried to remove session, but it was already gone..");
        }
        logger.log(Level.INFO, "Client disconnected. Total {0}", getSessionCount());
    }
    @Lock(LockType.READ)
    public void broadcastObject(Session sendingSession, Object o){
        for (Session session : sessionIndex) {
            if (!sendingSession.equals(session)){
                sendObject(session, o);
            }
        }
    }
    @Lock(LockType.READ)
    public int getSessionCount(){
        return sessionIndex.size();
    }
}
