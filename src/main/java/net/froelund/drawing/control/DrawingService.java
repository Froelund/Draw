package net.froelund.drawing.control;

import net.froelund.drawing.entity.ColorInfo;
import net.froelund.drawing.entity.Drawing;
import net.froelund.drawing.entity.DrawingPoint;

import javax.ejb.Singleton;
import javax.websocket.Session;
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

    List<Session> sessions = new CopyOnWriteArrayList<>();
    Drawing drawing = new Drawing(new ConcurrentHashMap<DrawingPoint, ColorInfo>());

    private static final Logger logger = Logger.getLogger(DrawingService.class.getName());

    public Drawing getDrawing(){
        return drawing;
    }

    public void addDrawing(Drawing drawing) {
        logger.log(Level.INFO, "New drawing submitted");
        mergeDrawing(drawing);
    }
    void mergeDrawing(Drawing drawing){
        this.drawing.getData().putAll(drawing.getData());
        logger.log(Level.INFO, "Merging {0} objects into main drawing", drawing.getData().size());
    }
}
