package net.froelund.drawing.boundary;

import net.froelund.drawing.control.DrawingService;
import net.froelund.drawing.control.SessionService;
import net.froelund.drawing.control.encoding.DrawingDecoder;
import net.froelund.drawing.control.encoding.DrawingEncoder;
import net.froelund.drawing.entity.Drawing;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by froelund on 5/29/14.
 */
@ServerEndpoint(
        value = "/drawing",
        encoders = DrawingEncoder.class,
        decoders = DrawingDecoder.class
)
@Stateless
public class DrawWSServer {

    @Inject
    private DrawingService drawingService;
    @Inject
    private SessionService sessionService;

    private static final Logger logger = Logger.getLogger(DrawWSServer.class.getName());

    @OnMessage
    public void onMessage(Drawing message, Session sendingSession){
        if (message.getData().size() > 0){
            drawingService.addDrawing(message);
            sessionService.broadcastObject(sendingSession, message);
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        sessionService.clientConnect(session);
        sessionService.sendObject(session, drawingService.getDrawing());
    }

    @OnClose
    public void onClose(Session session) {
        sessionService.clienDisconnect(session);
    }
}
