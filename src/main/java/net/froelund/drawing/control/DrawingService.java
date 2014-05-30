package net.froelund.drawing.control;

import net.froelund.drawing.entity.ColorInfo;
import net.froelund.drawing.entity.Drawing;
import net.froelund.drawing.entity.DrawingPoint;

import javax.ejb.Singleton;
import javax.websocket.EncodeException;
import javax.websocket.Session;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by froelund on 5/29/14.
 */
@Singleton
public class DrawingService {

    private List<Session> sessions = new CopyOnWriteArrayList<>();
    private Drawing drawing = new Drawing(new ConcurrentHashMap<DrawingPoint, ColorInfo>());

    private static final Logger logger = Logger.getLogger(DrawingService.class.getName());

    public void onClientConnect(Session session){
        sendDrawing(session, this.drawing);
        sessions.add(session);
        logger.log(Level.INFO, "Client connected. Total {0}", getSessionCount());
    }
    public void onClientDisconnect(Session session){
        sessions.remove(sessions.indexOf(session));
        logger.log(Level.INFO, "Client disconnected. Total {0}", getSessionCount());
    }
    public int getSessionCount(){
        return sessions.size();
    }
    public void addDrawing(Session sendingSession, Drawing drawing) {
        logger.log(Level.INFO, "New drawing submitted");
        mergeDrawing(drawing);
        int sendSessionCount = 0;
        for (Session receiverSession : sessions) {
            if (!receiverSession.equals(sendingSession)){
                sendDrawing(receiverSession, drawing);
                sendSessionCount++;
            }
        }
        logger.log(Level.INFO, "Drawing send to {0} sessions.", sendSessionCount);
    }
    void mergeDrawing(Drawing drawing){
        this.drawing.getData().putAll(drawing.getData());
        logger.log(Level.INFO, "{0}, drawing objects merged into main drawing", drawing.getData().size());
    }

    void broadcastDrawing(Drawing message){
        for (Session session : sessions) {
            sendDrawing(session, drawing);
        }
    }

    void sendDrawing(Session session, Drawing drawing){
        try {
            session.getBasicRemote().sendObject(drawing);
        } catch (IOException | EncodeException e) {
            logger.log(Level.INFO, "Error sending drawing, disconnecting session");
            onClientDisconnect(session);
        }
    }
}
