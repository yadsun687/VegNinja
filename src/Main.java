import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.Random;


public class Main extends Application{

    //Screen size
    public static int HEIGHT = Config.screenHEIGHT;
    public static int WIDTH = Config.screenWIDTH;

    public double spawnTimer = 0; //timer for fruit spawning
    public boolean isMouseDragged = false;
    public boolean isMouseClicked = false;
    public boolean isRunning = true; //game state

    //for rendering each frame
    long lastTime = System.nanoTime();
    public static double frameRate = Config.fps;
    public static double DeltaTime = 1.0/frameRate;
    double accumulateTime =0.0;


    //managing fruit
    ThrowerManager tm = new ThrowerManager();
    //cursor instance
    GameCursor cursor = new GameCursor();
    ParticleManager particleManager = ParticleManager.getInstance();
    //Media instance
    SoundManager soundManager = SoundManager.getInstance();

    public static Canvas canvas = new Canvas(WIDTH, HEIGHT); //canvas for drawing
    public static GraphicsContext gc = canvas.getGraphicsContext2D(); //we'll draw things using this


    //draw every fruit in fruitList
    public void drawFruit(ArrayList<Fruit> fruits){

        for(int i = 0 ; i < fruits.size() ; i++) {
            Fruit f = fruits.get(i);
            //will remove fruit which exists for too long
            if (f.checkTimeout()) {
                fruits.remove(i);
                i--;
                continue;
            }

            if (cursor.isClicked() && cursor.isInFruit(f)) {//break fruit when hit by cursor
                System.out.println("HIT");
                SoundManager.getInstance().playSound(0 , 1);
                gc.setFill(Color.web("RED"));
                gc.fillRect(f.getPosX(), f.getPosY(), f.getWidth(), f.getHeight());
                fruits.remove(i);
                i--;
            } else {
                gc.setFill(fruits.get(i).getColor());
                gc.fillRect(f.getPosX(), f.getPosY(), f.getWidth(), f.getHeight());
                f.setPosX(fruits.get(i).getPosX() + fruits.get(i).getVelX() * DeltaTime);
                f.setPosY(fruits.get(i).getPosY() + fruits.get(i).getVelY() * DeltaTime);
                f.update();
            }
        }
    }


    //this method will update every frame
    public void run(GraphicsContext gc){

        //draw grey background
        gc.setFill((Color.web("Grey")));
        gc.fillRect(0,0,WIDTH,HEIGHT);

        //spawn fruit every 1.5 sec
        spawnTimer += DeltaTime;
        if(spawnTimer>2){
            //random number to generate
            Random rand = new Random();
            int totalGenerated = rand.nextInt(2,5);
            for(int i = 0 ; i<totalGenerated ; i++){
                tm.createFruits();
            }
            spawnTimer=0;
        }
        drawFruit(tm.getFruits());

        //debug text
        gc.setFont(new Font("Segoe UI Black",32));
        gc.setFill(Color.web("Black"));
        gc.fillText("FruitsSize: " + tm.getFruits().size() + "\nmouse(X,Y): " + cursor.getPosX()+","+cursor.getPosY(), 20 , 70);

        ParticleManager.getInstance().particleUpdate();
        cursor.drawCursor();

    }


    //Class for handle rendering each frame
    class GameAnimationTimer extends AnimationTimer{

        @Override
        public void handle(long now) {
            double elapsedTime = (now-lastTime)/1e9; //from millisec to sec
            lastTime = now;

            accumulateTime+=elapsedTime;
            while(accumulateTime >= DeltaTime) { //will run with frame capped

                //draw grey background
                gc.setFill((Color.web("Grey")));
                gc.fillRect(0, 0, WIDTH, HEIGHT);

                //spawn fruit every 1.5 sec
                spawnTimer += DeltaTime;
                if (spawnTimer > 2) {
                    //random number to generate
                    Random rand = new Random();
                    int totalGenerated = rand.nextInt(2, 5);
                    for (int i = 0; i < totalGenerated; i++) {
                        tm.createFruits();
                    }
                    spawnTimer = 0;
                }
                drawFruit(tm.getFruits());

                //draw cursor and its particles
                ParticleManager.getInstance().particleUpdate();
                cursor.drawCursor();

                //debug text
                gc.setFont(new Font("Segoe UI Black", 32));
                gc.setFill(Color.web("WHITE"));
                gc.fillText("FPS: " + (1.0 / elapsedTime) + "\nbtn Y: " + tm.getFruits().size() + "\nmouse(X,Y): " + cursor.getPosX() + "," + cursor.getPosY(), 20, 70);

                accumulateTime-= DeltaTime;
            }
        }
    }
    GameAnimationTimer animationTimer = new GameAnimationTimer();



    @Override
    public void start(Stage primaryStage) {


        //add every sound to list
        SoundManager.getInstance().addMedia("file:/C:/Users/asus/IdeaProjects/FXtest/Sound/sfx/Fruit_Slash.mp3");
        SoundManager.getInstance().addMedia("file:/C:/Users/asus/IdeaProjects/FXtest/Sound/sfx/Knife_Whoosh.mp3");


        GridPane root = new GridPane();

        //make in-game pause button
        Button pauseBtn = new Button("||");
        pauseBtn.setTextFill(Color.web("BLACK"));
        pauseBtn.setFont(Font.font("Segoe UI BLACK" , 20));
        pauseBtn.setTranslateY((HEIGHT - pauseBtn.getLayoutY()));
        pauseBtn.setOnAction(actionEvent -> {
            if(isRunning) {
                animationTimer.stop();
                isRunning=false;

            }
            else {
                animationTimer.start();
                isRunning=true;
                lastTime=System.nanoTime();
            }
        });


        //scene -> root -> canvas
        root.getChildren().addAll(canvas , pauseBtn);
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        //=======<user input>===========================

        scene.setOnMouseMoved(event->{
            cursor.setPosX(event.getSceneX());
            cursor.setPosY(event.getSceneY());
        });
        scene.setOnMouseDragged(event->{
            if(!isMouseClicked) {
                SoundManager.getInstance().playSound(1, 1);
                isMouseClicked=true;
            }
            isMouseDragged=true;
            cursor.setClicked(true);
            cursor.setPosX(event.getSceneX());
            cursor.setPosY(event.getSceneY());
            cursor.makeTrailEffect();
        });
        scene.setOnMouseReleased(event->{
            isMouseDragged=false;
            isMouseClicked=false;
            cursor.setClicked(false);
        });

        //==============================================

        animationTimer.start();

        //============OLD RENDERING METHOD==============
        //Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(DeltaTime), e -> run(gc)));
        //timeline.setCycleCount(Timeline.INDEFINITE);
        //timeline.play();
        //==============================================

        //put 'scene' on stage and start render
        primaryStage.setTitle("SOMEGAME");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args){
        launch(args);
    }
}
