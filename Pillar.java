/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    
//    arc.setCenterX(SCENEWIDTH / 2);
//    arc.setCenterY(SCENEHEIGHT);
//    arc.setRadiusX(280.0f);
//    arc.setRadiusY(230.0f);
    
    public Pillar(double angle, double dist)
    {
        this.dist = dist;
        this.angle = angle;
        int x0 = 1920 / 2;
        int y0 = 1080;
        double x = dist * Math.cos(angle);
        double y = dist * Math.sin(angle);
        Circle c = new Circle();
        c.setFill(Color.RED);
        c.setRadius(10);
        
        
    }
}
