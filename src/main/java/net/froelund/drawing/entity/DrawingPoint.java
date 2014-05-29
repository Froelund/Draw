package net.froelund.drawing.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.awt.*;
import java.beans.Visibility;

/**
 * Created by froelund on 5/26/14.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DrawingPoint {
    @XmlElement
    private int x;
    @XmlElement
    private int y;

    public DrawingPoint(String point){
        this(Integer.parseInt(point.split(",")[0]), Integer.parseInt(point.split(",")[1]));
    }
    public DrawingPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public DrawingPoint() {
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return x + "," + y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DrawingPoint that = (DrawingPoint) o;

        if (x != that.x) return false;
        if (y != that.y) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

}
