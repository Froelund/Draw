package net.froelund.drawing.control;

import net.froelund.drawing.entity.ColorInfo;
import net.froelund.drawing.entity.Drawing;
import net.froelund.drawing.entity.DrawingPoint;

import javax.ejb.Singleton;
import javax.websocket.EncodeException;
import javax.websocket.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by froelund on 5/29/14.
 */
@Singleton
public class DrawingService {

    private List<Session> sessions = new ArrayList<>();
    private Drawing drawing = new Drawing();

    private static final Logger logger = Logger.getLogger(DrawingService.class.getName());

    public void onClientConnect(Session session){
        sendDrawing(session, this.drawing);
        sessions.add(session);
    }
    public void onClientDisconnect(Session session){
        sessions.remove(sessions.indexOf(session));
    }
    public int getSessionCount(){
        return sessions.size();
    }
    public void addDrawing(Session sendingSession, Drawing drawing) {
        mergeDrawing(drawing);
        for (Session receiverSession : sessions) {
            if (!receiverSession.equals(sendingSession)){
                sendDrawing(receiverSession, drawing);
            }
        }
    }
    void mergeDrawing(Drawing drawing){
        this.drawing.getData().putAll(drawing.getData());
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
