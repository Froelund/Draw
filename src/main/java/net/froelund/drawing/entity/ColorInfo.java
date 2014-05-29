package net.froelund.drawing.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by froelund on 5/27/14.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ColorInfo {
    @XmlElement
    private int r;
    @XmlElement
    private int g;
    @XmlElement
    private int b;
    @XmlElement
    private int transparency;

    public ColorInfo(int r, int g, int b, int transparency) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.transparency = transparency;
    }

    public ColorInfo() {
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public int getTransparency() {
        return transparency;
    }

    public void setTransparency(int transparency) {
        this.transparency = transparency;
    }
}
