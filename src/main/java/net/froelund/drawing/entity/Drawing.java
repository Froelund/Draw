package net.froelund.drawing.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by froelund on 5/26/14.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Drawing {
    @XmlElement
    private Map<DrawingPoint, ColorInfo> data;

    public Drawing() {
    }

    public Drawing(Map<DrawingPoint, ColorInfo> data) {
        this.data = data;
    }

    public Map<DrawingPoint, ColorInfo> getData() {
        if (data==null){
            data = new ConcurrentHashMap<>();
        }
        return data;
    }

    public void setData(Map<DrawingPoint, ColorInfo> data) {
        this.data = data;
    }
}
