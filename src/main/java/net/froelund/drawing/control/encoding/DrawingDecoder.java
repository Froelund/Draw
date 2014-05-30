package net.froelund.drawing.control.encoding;

import net.froelund.drawing.entity.ColorInfo;
import net.froelund.drawing.entity.Drawing;
import net.froelund.drawing.entity.DrawingPoint;

import javax.json.*;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.io.Reader;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by froelund on 5/28/14.
 */
public class DrawingDecoder implements Decoder.TextStream<Drawing> {

    private static final Logger logger = Logger.getLogger(DrawingDecoder.class.getName());

    @Override
    public Drawing decode(Reader reader) {
        Drawing drawing = new Drawing();
        try(JsonReader jsonReader = Json.createReader(reader)){
            JsonObject jsonDrawing = jsonReader.readObject();
            JsonObject drawingJsonData = jsonDrawing.getJsonObject("data");
            if (drawingJsonData != null){
                for (Map.Entry<String, JsonValue> entry : drawingJsonData.entrySet()){
                    JsonObject jsonObject = (JsonObject) entry.getValue();
                    drawing.getData().put(new DrawingPoint(entry.getKey()), new ColorInfo(jsonObject.getInt("r"), jsonObject.getInt("g"), jsonObject.getInt("b"), jsonObject.getInt("transparency")));
                }
            }
        } catch (JsonException e){
            logger.log(Level.INFO, "Invalid json pushed.");
        }
        return drawing;
    }

    @Override
    public void init(EndpointConfig config) {

    }

    @Override
    public void destroy() {

    }
}
