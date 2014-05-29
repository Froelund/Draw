package net.froelund.drawing.control.encoding;

import net.froelund.drawing.entity.ColorInfo;
import net.froelund.drawing.entity.Drawing;
import net.froelund.drawing.entity.DrawingPoint;

import javax.json.*;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.Map;

/**
 * Created by froelund on 5/28/14.
 */
public class DrawingDecoder implements Decoder.TextStream<Drawing> {

    @Override
    public Drawing decode(Reader reader) throws DecodeException, IOException {

        JsonReader jsonReader = Json.createReader(reader);
        JsonObject jsonDrawing = jsonReader.readObject();

        Drawing drawing = new Drawing();
        JsonObject drawingJsonData = jsonDrawing.getJsonObject("data");
        if (drawingJsonData != null){
            for (Map.Entry<String, JsonValue> entry : drawingJsonData.entrySet()){
                JsonObject jsonObject = (JsonObject) entry.getValue();
                drawing.getData().put(new DrawingPoint(entry.getKey()), new ColorInfo(jsonObject.getInt("r"), jsonObject.getInt("g"), jsonObject.getInt("b"), jsonObject.getInt("transparency")));
            }
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
