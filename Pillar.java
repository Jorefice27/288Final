package pkg228gui;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 *
 * @author johnn_000
 */
public class Pillar {
    
    private Circle c;
    private double dist;
    private double angle;
    public double width;
    
//    arc.setCenterX(SCENEWIDTH / 2);
//    arc.setCenterY(SCENEHEIGHT);
//    arc.setRadiusX(240.0f);
//    arc.setRadiusY(240.0f);
    
    public Pillar(double angle, double dist, double width)
    {
        this.dist = dist;
        this.angle = angle;
        this.width = width;
        int x0 = 1920 / 2;
        int y0 = 1080;
        int cm = 240 / 80;
        double x = dist * Math.cos(angle);
        double y = dist * Math.sin(angle);
        Circle c = new Circle();
        if(isTargetPillar())
        {
            c.setFill(Color.LAWNGREEN);
        }
        else
        {
            c.setFill(Color.RED);
        }
        c.setRadius(10);
        c.setCenterX(x0 + (x * cm));
        c.setCenterY(y0 + (y * cm));   
    }
    
    public Circle getCircle()
    {
        return c;
    }
    
    public double centerX()
    {
        return c.getCenterX();
    }
    
    public double centerY()
    {
        return c.getCenterY();
    }
    
    public boolean isTargetPillar()
    {
        return width < 35;
    }
}