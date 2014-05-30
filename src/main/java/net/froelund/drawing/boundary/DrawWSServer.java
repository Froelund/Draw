package net.froelund.drawing.boundary;

import net.froelund.drawing.control.DrawingService;
import net.froelund.drawing.control.encoding.DrawingDecoder;
import net.froelund.drawing.control.encoding.DrawingEncoder;
import net.froelund.drawing.entity.Drawing;

import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

    private static final Logger logger = Logger.getLogger(DrawWSServer.class.getName());

    @OnMessage
    public void onMessage(Drawing message, Session sendingSession){
        drawingService.addDrawing(sendingSession, message);
    }

    @OnOpen
    public void onOpen(Session session) {
        drawingService.onClientConnect(session);
    }

    @OnClose
    public void onClose(Session session) {
        drawingService.onClientDisconnect(session);
    }
}
