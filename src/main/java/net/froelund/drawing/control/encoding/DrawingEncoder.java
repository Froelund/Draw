package net.froelund.drawing.control.encoding;

import net.froelund.drawing.entity.ColorInfo;
import net.froelund.drawing.entity.Drawing;
import net.froelund.drawing.entity.DrawingPoint;

import javax.json.*;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import java.util.Map;

/**
 * Created by froelund on 5/28/14.
 */
public class DrawingEncoder implements Encoder.Text<Drawing>{

    @Override
    public void init(EndpointConfig config) {
    }

    @Override
    public void destroy() {

    }

    @Override
    public String encode(Drawing object) throws EncodeException {
        JsonObjectBuilder dataBuilder = Json.createObjectBuilder();
        for (Map.Entry<DrawingPoint, ColorInfo> entry : object.getData().entrySet()) {
            dataBuilder.add(entry.getKey().toString(), Json.createObjectBuilder().
                            add("r", entry.getValue().getR()).
                            add("g", entry.getValue().getG()).
                            add("b", entry.getValue().getB()).
                            add("transparency", entry.getValue().getTransparency())
            );
        }
        return Json.createObjectBuilder().add(
                "data", dataBuilder.build()
        ).build().toString();

    }
}
