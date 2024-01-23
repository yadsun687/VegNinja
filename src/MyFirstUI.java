import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Window;
import javafx.util.Duration;

import java.awt.*;
import java.awt.geom.Point2D;
import java.security.Key;
import java.util.ArrayList;
import java.util.Random;


public class MyFirstUI extends Application{
    public static int HEIGHT =600;
    public static int WIDTH = 720;
    public static double DeltaTime = 0.0167;
    public static final double gravity = 60; //gravity for player
    public double spawnTimer = 0; //timer for fruit spawing


    Player p1 = new Player("UncleBob",Color.web("Lightblue"),50,50, 30,60,10,10);
    ArrayList<object> objList = new ArrayList<>();
    object brownGround = new object(0,550,WIDTH,50,Color.web("Brown"));
    object purpleWall = new object(300,450,50,100,Color.web("Purple"));
    object greyGround = new object(40,350,60,50,Color.web("White"));

    //managing fruit
    ThrowerManager tm = new ThrowerManager();


    Canvas canvas = new Canvas(WIDTH, HEIGHT); //canvas for drawing
    GraphicsContext gc = canvas.getGraphicsContext2D(); //we'll draw things using this


    public class object{
        public double x;
        public double y;
        public double width;
        public double height;
        public Color color;

        public object(double x , double y , double width, double height , Color color){
            this.x=x;
            this.y=y;
            this.width=width;
            this.height=height;
            this.color=color;
        }

        public void draw(){
            gc.setFill(color);
            gc.fillRect(x,y,width,height);
        }
    }

    public class Player{
        public String name;
        public Color color;
        public double posX;
        public double posY;
        public double pWidth;
        public double pHeight;
        public double pAWidth;
        public double pAHeight;

        public double velX = 0;
        public double velY = 0;

        public boolean isMoveLeft = false;
        public boolean isMoveRight= false;

        public boolean isJump= false;
        public boolean isJumping = false;
        public boolean isOnGround= false;
        public boolean isDash= false;
        public double dashInterval = 0;

        public Player(String name, Color color , double posX, double posY, double pWidth, double pHeight, double pAWidth, double pAHeight) {
            this.name = name;
            this.color=color;
            this.posX = posX;
            this.posY = posY;
            this.pWidth = pWidth;
            this.pHeight = pHeight;
            this.pAWidth = pAWidth;
            this.pAHeight = pAHeight;
        }

        public void movementHandling(){

            if(isDash && (isMoveLeft||isMoveRight)){
                isDash=false;
                velX= isMoveLeft? -1000:1000;
            }
            else if(isMoveLeft && velX>=-300) {
                velX -= 60;
            }
            else if(isMoveRight && velX<=300) {
                velX += 60;
            }


            if(isJump && isOnGround) {//jump
                isJump=false;
                isJumping=true;
                isOnGround=false;
                velY = -900;
            } else if (isJump&&isJumping) {//doubleJump
                isJump=false;
                isJumping=false;
                velY = -900;
            }

            //position displaces
            posX+=velX*DeltaTime;
            posY+=velY*DeltaTime;

            if(velX!=0) velX = velX>0? velX-30 : velX+30;//reduce velX

            if(velY<=700) velY += gravity;

        }

        public void checkCollision(ArrayList<object> objList){
            for(object obj : objList){ //check every object in list
                double player_bot = posY+pHeight;
                double obj_bot = obj.y+obj.height;
                double player_right=posX+pWidth;
                double obj_right=obj.x+obj.width;

                if(((obj.x<posX&&posX<obj_right) || (obj.x<player_right&&player_right<obj_right)) &&
                        ((obj.y<posY&&posY<obj_bot) || (obj.y<player_bot&&player_bot<obj_bot))){

                    double b_col = Math.abs(obj_bot - posY);
                    double t_col = Math.abs(player_bot - obj.y);
                    double l_col = Math.abs(player_right - obj.x);
                    double r_col = Math.abs(obj_right - posX);

                    if (t_col < b_col && t_col < l_col && t_col < r_col && velY > 0) {//Top collision
                        posY = obj.y - pHeight;
                        velY = 0;
                        isOnGround = true;


                    }
                    if (b_col < t_col && b_col < l_col && b_col < r_col && velY < 0) {//bottom collision
                        velY = 0;
                        posY = obj_bot;

                    }
                    if (l_col < r_col && l_col < t_col && l_col < b_col && velX > 0) {//Left collision
                        velX = 0;
                        posX = obj.x-pWidth;

                    }
                    if (r_col < l_col && r_col < t_col && r_col < b_col && velX < 0) {//Right collision
                        velX = 0;
                        posX = obj_right;

                    }
                }
            }
        }

        public void physicHandling(){
            if(!isOnGround) velY+=5;
        }

        public void draw(){
            gc.setFill(color);
            gc.fillRoundRect(posX,posY,pWidth,pHeight,pAWidth,pAHeight);
        }

        public void checkWall(){
            if(posY+pHeight>HEIGHT){//touch floor
                isOnGround=true;
                velY=0;
                posY=HEIGHT-pHeight;
            }
            if (posX <= 0) {//touch left screen
                velX = 0;
                posX = 0;
            }
            if (posX + pWidth >= WIDTH) {//touch right screen
                velX = 0;
                posX = WIDTH - pWidth;
            }
        }

        public void cooldownHandle(){
            if(dashInterval>=0)dashInterval-=DeltaTime;
                else dashInterval=0;
            System.out.println("YES");
        }

        public void update(){

            cooldownHandle();
            movementHandling();
            physicHandling();
            checkCollision(objList);
            checkWall();
            draw();

        }
    }

    //draw every object in objList
    public void drawObject(ArrayList<object> objList){
        for(object obj : objList){
            obj.draw();
        }
    }

    //draw every fruit in fruitList
    public void drawFruit(ArrayList<Fruit> fruits){
        for(int i = 0 ; i < fruits.size() ; i++){
            Fruit f = fruits.get(i) ;
            //will remove fruit which exists for too long
            if(fruits.get(i).checkTimeout()){
                fruits.remove(i);
                i--;
                continue;
            }
            gc.setFill(fruits.get(i).getColor());
            gc.fillRect(f.getPosX() , f.getPosY()  , f.getWidth() , f.getHeight() );
            f.setPosX(fruits.get(i).getPosX()+fruits.get(i).getVelX()*DeltaTime);
            f.setPosY(fruits.get(i).getPosY()+fruits.get(i).getVelY()*DeltaTime);
            f.update();
        }
    }

    //this method will update every frame
    public void run(GraphicsContext gc){

        //draw grey background
        gc.setFill((Color.web("Grey")));
        gc.fillRect(0,0,WIDTH,HEIGHT);

        //spawn fruit every 1.5 sec
        spawnTimer += DeltaTime;
        if(spawnTimer>1.5){
            //random number to generate
            Random rand = new Random();
            int totalGenerated = rand.nextInt(2,5);
            for(int i = 0 ; i<totalGenerated ; i++){
                tm.createFruits();
            }
            spawnTimer--;
        }
        drawFruit(tm.getFruits());


        //player update
        p1.update();
        p1.dashInterval+=DeltaTime;

        //debug text
        gc.setFont(new Font("Segoe UI Black",32));
        gc.setFill(Color.web("Black"));
        gc.fillText("spawnTimer:" + spawnTimer + "\nFruitsSize: " + tm.getFruits().size(), 20 , 30);

    }

    @Override
    public void start(Stage primaryStage) {


        //objList.add(purpleWall);
        //objList.add(brownGround);
        //objList.add(greyGround);

        StackPane root = new StackPane();
        root.getChildren().addAll(canvas);
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        //scene -> root -> canvas

        //user input
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode() == KeyCode.A){
                    p1.isMoveLeft=true;
                }
                if(keyEvent.getCode()==KeyCode.D){
                    p1.isMoveRight=true;
                }
                if(keyEvent.getCode()==KeyCode.SPACE){
                    p1.isJump=true;
                }
                if(keyEvent.getCode()==KeyCode.SHIFT && p1.dashInterval<=0 && (p1.isMoveLeft|| p1.isMoveRight)){
                    p1.isDash=true;
                    p1.dashInterval=0.75;
                }


            }
        });
        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode() == KeyCode.A){
                    p1.isMoveLeft=false;
                }
                if(keyEvent.getCode()==KeyCode.D){
                    p1.isMoveRight=false;
                }

            }
        });

        //set framerate for rendering
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(DeltaTime), e -> run(gc)));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        //put 'scene' on stage and start render
        primaryStage.setTitle("SOMEGAME");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args){
        launch(args);
    }
}
