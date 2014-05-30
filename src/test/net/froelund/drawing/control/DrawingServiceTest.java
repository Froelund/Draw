package net.froelund.drawing.control;

import net.froelund.drawing.entity.ColorInfo;
import net.froelund.drawing.entity.Drawing;
import net.froelund.drawing.entity.DrawingPoint;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;

public class DrawingServiceTest {
    @Test
    public void testMergeDrawing(){
        DrawingService drawingService = new DrawingService();
        drawingService.mergeDrawing(new Drawing(Collections.singletonMap(new DrawingPoint("1,1"), new ColorInfo(255,255,255,255))));
        Assert.assertThat("Drawing wasn't merged", drawingService.getDrawing().getData().size(), CoreMatchers.is(1));
    }

}