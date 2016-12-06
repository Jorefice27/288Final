package pkg228gui;



import ch.aplu.xboxcontroller.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.shape.Arc;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.ImageInput;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import pkg228gui.Link;

/**
 *
 * @author johnn_000
 */
public class GUI extends Application{
    
    private final int SCENEWIDTH = 1920 ;
    private final int SCENEHEIGHT = 1080;
    private double distance;
    private Rectangle bg;
    private Rectangle detectionTextBox;
    private Rectangle detectionAlertBox;
    private Rectangle botStatusBox;
//    private ArrayList<Pillar> pillars;
//    private ArrayList<Circle> mapPoints;
    private Pillar[] pillars = new Pillar[5];
    private Circle[] mapPoints = new Circle[5];
    private static Rectangle connect;
    private Arc arc;
    private Group root;
    private Text mainHUDtext;
    private static Text leftBumper;
    private static Text rightBumper;
    private static Text lightSensor;
    private static Text stopText;
    private static Text moveText;
    private static Text turnTextL;
    private static Text turnTextR;
    private static Text sweepText;
    private static Text connectText;
    private ArrayList<Text> detectionText;
    private Link comm;
    private Controller controller;
    private boolean connected;
    private boolean firstClick = true;
    
    private enum event {LEFTBUMP, RIGHTBUMP, LIGHTSENS, MOVING, STOPPED, SWEEP, LEFTTURN, RIGHTTURN}; 

    @Override
    public void start(Stage stage) throws Exception
    {
        setBackground();       
        makeMap();
        setMainHUDtext();
        setDetectionTextBox();        
        setDetectionAlertBox();
        setStatusBox();
        makeConnect();
        
        
        setGroup();
        controller = new Controller();
        //receive data from bot
        CommReceive r = new CommReceive();
        new Thread(r).start();
        
        
        
        //Create scene
        Scene scene = new Scene(root, SCENEWIDTH, SCENEHEIGHT);
        
        //Set up button input
        EventHandler<KeyEvent> handleController = new EventHandler<KeyEvent>()
        {
           @Override
           public void handle(KeyEvent e)
           {
               updateDetectionText(e.getCode().toString());
//               updateDetectionText(e.getText());
               //wasd = dpad
               //move forward
               if(e.getText().equals("w"))
               {
                   comm.sendByte('1');
               }
               //move back
               if(e.getText().equals("s"))
               {
                   comm.sendByte('2');
               }
               //Turn left
               if(e.getText().equals("a"))
               {
                   comm.sendByte('3');
               }
               //Turn Right
               if(e.getText().equals("d"))
               {
                   comm.sendByte('4');
               }
               //Sweep
               if(e.getCode().toString().equals("SPACE"))
               {
                   comm.sendByte('5');
               }
               //Move 5 cm
               if(e.getText().equals("1"))
               {
                   comm.sendByte('a');
               }
               //Move 10 cm
               if(e.getText().equals("2"))
               {
                   comm.sendByte('b');
               }
               //Move 15 cm
               if(e.getText().equals("3"))
               {
                   comm.sendByte('c');
               }
               //Move 20 cm
               if(e.getText().equals("4"))
               {
                   comm.sendByte('d');
               }
           }
        };
        scene.setOnKeyPressed(handleController);
        scene.setFill(Color.WHITE);
        
        //Set title
        stage.setTitle("GUI");
        stage.setTitle("VORTEX Headquarters");
        
        //Add scene to stage
        stage.setScene(scene);
        
        //Display stage
        stage.show();
        
    }
    
    private void setBackground()
    {
        //Load background
        Image image = new Image("http://wallpaperswide.com/download/hud_1_0-wallpaper-1920x1080.jpg");
        bg = new Rectangle();
        
        ImageInput imageInput = new ImageInput();
        imageInput.setX(0);
        imageInput.setY(0);
        imageInput.setSource(image);
        bg.setEffect(imageInput);
        
    }
    
    private void makeMap()
    {
        //Create arc
        arc = new Arc();
        arc.setCenterX(SCENEWIDTH / 2);
        arc.setCenterY(SCENEHEIGHT);
        arc.setRadiusX(240.0f);
        arc.setRadiusY(240.0f);
        arc.setStartAngle(0.0f);
        arc.setLength(180.0f); //degrees 
        arc.setFill(Color.AQUA);
        arc.setOpacity(0.8);
        arc.setType(ArcType.ROUND);
        for(int i = 0; i < mapPoints.length; i++)
        {
//            Pillar p = new Pillar(30 * (1 + i), 40, 45 * 1);
//            pillars[i] = p;
//            mapPoints[i] = pillars[i].getCircle();
            mapPoints[i] = new Circle();
        }
    }
    
    private void setMainHUDtext()
    {
        mainHUDtext = new Text();
        mainHUDtext.setText("VOTEX Mission Control HUD");
        mainHUDtext.setFont(Font.font("sans-serif", FontWeight.BOLD, FontPosture.REGULAR, 40));
        mainHUDtext.setX(SCENEWIDTH * 0.363); //figure out how to actually center text                        <-----------------------
        mainHUDtext.setY(SCENEHEIGHT * 0.045);
        mainHUDtext.setFill(Color.CADETBLUE);
    }
    
    private void setDetectionTextBox()
    {
        //set up detection text box
        detectionTextBox = new Rectangle();
        detectionTextBox.setX(SCENEWIDTH * 0.05);
        detectionTextBox.setY(SCENEHEIGHT * 0.1);
        detectionTextBox.setWidth(700);
        detectionTextBox.setHeight(350);
        detectionTextBox.setArcWidth(detectionTextBox.getWidth()* 0.0714);
        detectionTextBox.setArcHeight(detectionTextBox.getHeight() * 0.114);
        detectionTextBox.setFill(Color.CYAN);
        detectionTextBox.setOpacity(0.30);
        
        //add text
        detectionText = new ArrayList<Text>();
        int offset = 0;
        
        for(int i = 0; i < 5; i++)
        {
            Text text = new Text("");
            text.setX(SCENEWIDTH * 0.06);
            text.setY(SCENEHEIGHT * 0.13 + offset);
            text.setFont(Font.font("sans-serif", FontWeight.BOLD, FontPosture.REGULAR, 22));
            text.setFill(Color.AQUA);
            detectionText.add(text);
            offset += 75;
        }
        
    }
    
    private void setDetectionAlertBox()
    {
        //Create Alert Box
        detectionAlertBox = new Rectangle();
        detectionAlertBox.setX(SCENEWIDTH * 0.65);
        detectionAlertBox.setY(SCENEHEIGHT * 0.16);
        detectionAlertBox.setWidth(600);
        detectionAlertBox.setHeight(325);
        detectionAlertBox.setArcWidth(detectionAlertBox.getWidth()* 0.0714);
        detectionAlertBox.setArcHeight(detectionAlertBox.getHeight() * 0.114);
        detectionAlertBox.setFill(Color.CYAN);
        detectionAlertBox.setOpacity(0.3);
        
        //Set up left bumper alert
        leftBumper = new Text("Left Bumper Has Been Triggered");
        leftBumper.setFont(Font.font("sans-serif", FontWeight.BOLD, FontPosture.REGULAR, 30));
        leftBumper.setOpacity(0.1);
        leftBumper.setX(SCENEWIDTH * 0.67);
        leftBumper.setY(SCENEHEIGHT * 0.2);
        
        EventHandler<MouseEvent> handleLB = new EventHandler<MouseEvent>()
        {
         @Override
         public void handle(MouseEvent e)
         {
             triggerEvent(event.LEFTBUMP);
         }
        };
        
//        leftBumper.addEventFilter(InputEvent, handleLB);
        leftBumper.setOnMouseClicked(handleLB);
        
        //Set up right bumper alert
        rightBumper = new Text("Right Bumper Has Been Triggered");
        rightBumper.setFont(Font.font("sans-serif", FontWeight.BOLD, FontPosture.REGULAR, 30));
        rightBumper.setOpacity(0.1);
        rightBumper.setX(SCENEWIDTH * 0.67);
        rightBumper.setY(SCENEHEIGHT * 0.25);
        
        EventHandler<MouseEvent> handleRB = new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent e)
            {
                triggerEvent(event.RIGHTBUMP);
            }
        };
        
        rightBumper.setOnMouseClicked(handleRB);
        
        //Set up light sensor alert
        lightSensor = new Text("Light Sensor Has Been Triggered");
        lightSensor.setFont(Font.font("sans-serif", FontWeight.BOLD, FontPosture.REGULAR, 30));
        lightSensor.setOpacity(0.1);
        lightSensor.setX(SCENEWIDTH * 0.67);
        lightSensor.setY(SCENEHEIGHT * 0.3);
        
        EventHandler<MouseEvent> handleLS = new EventHandler<MouseEvent>()
        {
          @Override
          public void handle(MouseEvent e)
          {
                triggerEvent(event.LIGHTSENS);
          }
        };
        
        lightSensor.setOnMouseClicked(handleLS);
        
    }
    
    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //Alert Light Up Trigger Stuff
    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void triggerEvent(event e){
        LightMeUp test = null;
        if (e == event.LEFTBUMP) {
            test = new LightMeUp(leftBumper, 1500);
        }
        if (e == event.RIGHTBUMP) {
            test = new LightMeUp(rightBumper, 1500);
        }
        if (e == event.LIGHTSENS) {
            test = new LightMeUp(lightSensor, 1500);
        }
        if (test != null) {
            new Thread(test).start();
        }
    }
    
    private class LightMeUp implements Runnable {

        private Text textToLightUp;
        private int durationMili;
        
        public LightMeUp(Text textToLightUp, int durationMili) 
        {
            this.textToLightUp = textToLightUp;
            this.durationMili = durationMili;
        }

        @Override
        public void run()
        {
            try 
            {
                textToLightUp.setFill(Color.YELLOW);
                textToLightUp.setOpacity(1);
                Thread.sleep(durationMili);
                for(int i = 0; i < 90; i++)
                {
                    textToLightUp.setOpacity(textToLightUp.getOpacity() - .01);
                    Thread.sleep(8);
                }
                textToLightUp.setFill(Color.BLACK);
            } 
            catch (InterruptedException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    
    } 
    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    
    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //Receive Stuff 
    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void triggerReceive(event e)
    {
        CommReceive x = null;
        String message;
        try 
        {
            message = comm.recieveMessage();
        }
        catch (IOException ex)
        {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private class CommReceive implements Runnable
    {
        ArrayList<String> messages;
        
        public CommReceive(){};

        @Override
        public void run() {
            while(true)
            {
                try {
                    String message = comm.recieveMessage();
                    Scanner sc = new Scanner(message);
                    String scanned = sc.next();
                    if(scanned.equals("LB"))
                    {
                        //trigger left bumper
                        LightMeUp test = new LightMeUp(leftBumper, 1500);
                    }
                    if(scanned.equals("RB"))
                    {
                        LightMeUp test = new LightMeUp(rightBumper, 1500);
                    }
                    if(scanned.equals("BB"))
                    {
                        LightMeUp test = new LightMeUp(leftBumper, 1500);
                        test = new LightMeUp(rightBumper, 1500);
                    }
                    if(scanned.equals("LS"))
                    {
                        LightMeUp test = new LightMeUp(lightSensor, 1500);
                    }
                    if(scanned.equals("Stop"))
                    {
                        //update stoped notificaton
                    }
                    if(scanned.equals("Move"))
                    {
                        //update moving notification
                    }
                    if(scanned.equals("SS"))
                    {
                        //turn on sweeping notification                        
                    }
                    if(scanned.equals("FS"))
                    {
                        //turn off sweeping notification
                    }
                    if(scanned.equals("Object"))
                    {
                        int index = 0;
                        while(!scanned.equals("end"))
                        {
                            int angle = sc.nextInt();
                            int distance = sc.nextInt();
                            int width = sc.nextInt();
                            addPillar(angle, distance, width, index++);
                            scanned = sc.next();
                        }
                        updateMap();
                    }
                    
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private void setStatusBox()
    {
        
        //Set up status box
        botStatusBox = new Rectangle();
        botStatusBox.setX(SCENEWIDTH * 0.65);
        botStatusBox.setY(SCENEHEIGHT * 0.55);
        botStatusBox.setWidth(600); 
        botStatusBox.setHeight(225);
        botStatusBox.setArcWidth(botStatusBox.getWidth()* 0.0714);
        botStatusBox.setArcHeight(botStatusBox.getHeight() * 0.114);
        botStatusBox.setFill(Color.CYAN);
        botStatusBox.setOpacity(0.3);
        
        //Initialize sweep text
        sweepText = new Text("Sweeping");
        sweepText.setX(SCENEWIDTH * 0.66);
        sweepText.setY(SCENEHEIGHT * 0.58);
        sweepText.setFont(Font.font("sans-serif", FontWeight.BOLD, FontPosture.REGULAR, 30));
        sweepText.setFill(Color.BLACK);
        sweepText.setOpacity(0.1);
        
        //Initialize Moving Text
        moveText = new Text("Moving");
        moveText.setX(SCENEWIDTH * 0.66);
        moveText.setY(SCENEHEIGHT * 0.62);
        moveText.setFont(Font.font("sans-serif", FontWeight.BOLD, FontPosture.REGULAR, 30));
        moveText.setFill(Color.BLACK);
        moveText.setOpacity(0.1);
        
        stopText = new Text("Stopped");
        stopText.setX(SCENEWIDTH * 0.66);
        stopText.setY(SCENEHEIGHT * 0.66);
        stopText.setFont(Font.font("sans-serif", FontWeight.BOLD, FontPosture.REGULAR, 30));
        stopText.setFill(Color.BLACK);
        stopText.setOpacity(0.1);
        
        //Initialize turning text
        turnTextL = new Text("Turning Left");
        turnTextL.setX(SCENEWIDTH * 0.66);
        turnTextL.setY(SCENEHEIGHT * 0.7);
        turnTextL.setFont(Font.font("sans-serif", FontWeight.BOLD, FontPosture.REGULAR, 30));
        turnTextL.setFill(Color.BLACK);
        turnTextL.setOpacity(0.1);
        
        turnTextR = new Text("Turning Right");
        turnTextR.setX(SCENEWIDTH * 0.66);
        turnTextR.setY(SCENEHEIGHT * 0.74);
        turnTextR.setFont(Font.font("sans-serif", FontWeight.BOLD, FontPosture.REGULAR, 30));
        turnTextR.setFill(Color.BLACK);
        turnTextR.setOpacity(0.1);
        
        
        
        
        //Initialize turning text

    }
    
    private void makeConnect()
    {
        connect = new Rectangle();
        connect.setX(SCENEWIDTH * 0.05);
        connect.setY(SCENEHEIGHT * 0.872);
        connect.setWidth(400);
        connect.setHeight(130);
        connect.setArcWidth(connect.getWidth()* 0.0714);
        connect.setArcHeight(connect.getHeight() * 0.114);
        connect.setFill(Color.WHITE);
        connect.setOpacity(0.3);
        connectText = new Text("Connect");
        connectText.setX(SCENEWIDTH * 0.05 + 100);
        connectText.setY(SCENEHEIGHT * 0.95);
        connectText.setFont(Font.font("sans-serif", FontWeight.BOLD, FontPosture.REGULAR, 50));
        connectText.setFill(Color.WHITE);
        connectText.setOpacity(0.7);
        
        
        EventHandler<MouseEvent> handleConnect = new EventHandler<MouseEvent>()
        {
          
            @Override
            public void handle(MouseEvent e)
            {
                if(!connected)
                {
                    try
                    {
                        connectText.setText("Connecting...");
                        connectText.setFont(Font.font("sans-serif", FontWeight.BOLD, FontPosture.REGULAR, 30));
                        connectText.setX(SCENEWIDTH * 0.05 + 70);
                        connectText.setY(SCENEHEIGHT * 0.94);
                        comm = new Link();
                        
                        connected = true;
                    }
                    catch (IOException ex) 
                    {
//                        Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                        System.out.println("Connection failed. Restart program and try again.");
                    }
                    if(connected)
                    {
                        connect.setOpacity(0.0);
                        connectText.setOpacity(0.0);
                    }   
                }
            }
        };
        connect.setOnMouseClicked(handleConnect);
        connectText.setOnMouseClicked(handleConnect);
    }
        
    private void setGroup()
    {
        root = new Group(bg);
        ObservableList<Node> children = root.getChildren();
        children.add(mainHUDtext);
        children.add(arc);
        children.add(detectionTextBox);
        children.add(detectionAlertBox);
        children.add(botStatusBox);
        children.add(leftBumper);
        children.add(rightBumper);
        children.add(lightSensor);
        children.add(sweepText);   
        children.add(stopText);
        children.add(moveText);
        children.add(turnTextL);
        children.add(turnTextR);
        children.add(connect);
        children.add(connectText);
        children.addAll(detectionText);
//        children.addAll(mapPoints);
        for(int i = 0; i < mapPoints.length; i++)
        {
            children.add(mapPoints[i]);
        }
    }
    
    private void updateDetectionText(String text)
    {
//               detectionText.get(detectionTextIndex++).setText("You just pressed " + e.getText());
        for(int i = detectionText.size() - 1; i > 0; i--)
        {
            detectionText.get(i).setText(detectionText.get(i - 1).getText());
        }
        detectionText.get(0).setText(text);
    }
    
    private void updateMap()
    {
        for(int i = 0; i < mapPoints.length; i++)
        {
            mapPoints[i].setFill(Color.TRANSPARENT);
        }
        for(int i = 0; i < pillars.length; i++)
        {
            if(pillars[i] == null)
            {
                break;
            }
           mapPoints[i].setCenterX(pillars[i].centerX());
           mapPoints[i].setCenterY(pillars[i].centerY());
           mapPoints[i].setFill(pillars[i].getCircle().getFill());
           updateDetectionText("Object " + pillars[i].getDistance() + " cm away at " + pillars[i].getAngle() + " degrees");
        }
        pillars = new Pillar[5];
    }
    
    private void addPillar(double angle, double dist, double width, int index)
    {
       Pillar p = new Pillar(angle, dist, width);
       pillars[index] = p;
    }
    
    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //Private Pillar Class
    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private class Pillar {

    private Circle c;
    private double dist;
    private double angle;
    public double width;

    public Pillar(double angle, double dist, double width)
    {
        this.dist = dist;
        this.angle = angle;
        angle = (angle * 3.14159) / 180; //convert angle from degrees to radians
        this.width = width;
        int x0 = SCENEWIDTH / 2;
        int y0 = SCENEHEIGHT;
        double cm = 228 / 80f; //pixels per cm
        double x = dist * Math.cos(angle); //x dist in cm
        double y = dist * Math.sin(angle); //y dist in cm
        c = new Circle();
        c.setRadius(10);
        c.setCenterX(x0 + (x * cm));
        c.setCenterY(y0 - (y * cm));
        if(isTargetPillar())
        {
//            c.setFill(Color.LAWNGREEN);
            c.setFill(Color.SPRINGGREEN);
        }
        else
        {
            c.setFill(Color.RED);
        }
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
    
    public double getDistance()
    {
        return dist;
    }
    
    public double getAngle()
    {
        return angle;
    }
    
    public boolean isTargetPillar()
    {
        return width < 35;
    }
}
    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    
    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //Private Controller class
    private class Controller extends XboxControllerAdapter
    {
        private XboxController x;
        private int i;
        
        public Controller()
        {
            x = new XboxController();
            x.addXboxControllerListener(this);
            i = 0;
        }
        
        @Override
        public void rightTrigger(double value)
        {
            if(value == 1)
            {
                comm.sendByte('1');
            }
        }
        @Override
        public void leftTrigger(double value)
        {
            if(value == 1)
            {
                comm.sendByte('2');
            }
        }
        @Override
        public void leftShoulder(boolean pressed)
        {
            if(pressed)
            {
                comm.sendByte('3');
            }
        }
        @Override
        public void rightShoulder(boolean pressed)
        {
            if(pressed)
            {
                comm.sendByte('4');
            }
        }
        @Override
        public void buttonA(boolean pressed)
        {
            
        }
        
    }
    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public static void main(String[] args)
    {
        
        launch(args);
       
    }
}