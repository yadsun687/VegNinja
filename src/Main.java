//import javafx.animation.AnimationTimer;
//import javafx.animation.PauseTransition;
//import javafx.application.Application;
//import javafx.beans.property.BooleanProperty;
//import javafx.beans.property.LongProperty;
//import javafx.beans.property.SimpleBooleanProperty;
//import javafx.beans.property.SimpleLongProperty;
//import javafx.event.Event;
//import javafx.event.EventType;
//import javafx.fxml.FXMLLoader;
//import javafx.geometry.Insets;
//import javafx.geometry.Pos;
//import javafx.scene.Cursor;
//import javafx.scene.Node;
//import javafx.scene.Parent;
//import javafx.scene.canvas.Canvas;
//import javafx.scene.canvas.GraphicsContext;
//import javafx.scene.control.Button;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.scene.input.KeyCode;
//import javafx.scene.input.MouseEvent;
//import javafx.scene.layout.*;
//import javafx.scene.paint.Color;
//import javafx.scene.text.Font;
//import javafx.stage.Stage;
//import javafx.scene.Scene;
//import javafx.util.Duration;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Random;
//
//
//public class Main extends Application{
//
//    private static final double MIN_STATIONARY_TIME = 100_000_000;
//    //Screen size
//    public static int HEIGHT = Config.screenHEIGHT;
//    public static int WIDTH = Config.screenWIDTH;
//
//    public static double spawnTimer = 0; //timer for fruit spawning
//    public static boolean isMouseDragged = false;
//    public static boolean isMouseClicked = false;
//    public static boolean isRunning = true; //game state
//
//    //value for setting up game rendering (some magic to prevent inconsistent framerate)
//    public static double frameRate = Config.fps;
//    public static double DeltaTime = 1.0/frameRate;
//    public static double accumulateTime =0.0;
//
//    //create node to add into scene
//    public static StackPane gameScene = new StackPane(); //Our main game
//    public static Parent mainMenu;//main menu when the game booted
//    static {
//        try {
//            mainMenu = FXMLLoader.load(Main.class.getResource("MainMenu.fxml"));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    //arrayList of node for Scene switching
//    public static ArrayList<Parent> nodes = new ArrayList<>();
//
//    //managing fruit
//    public static ThrowerManager tm = new ThrowerManager();
//
//    //cursor instance
//    public static GameCursor cursor = new GameCursor();
//    public static ParticleManager particleManager = ParticleManager.getInstance();
//
//    //Media instance
//    SoundManager soundManager = SoundManager.getInstance();
//    public static ArrayList<Scene> Scenes = new ArrayList<>();
//
//    //init scene for adding another layer on it
//    public static Scene scene = new Scene(mainMenu, WIDTH , HEIGHT);
//    public static Canvas canvas = new Canvas(WIDTH, HEIGHT); //canvas for drawing
//    public static GraphicsContext gc = canvas.getGraphicsContext2D(); //we'll draw things using this
//
//    public static GameAnimationTimer animationTimer;
//
//    //load image
//    public static Image gamebackground = new Image(Main.class.getResource("Image/gameBackground1.png")
//            .toString(),WIDTH,HEIGHT,true,false);
//
//    public static ImageView gamePauseBtnIcon = new ImageView(
//                            new Image(Main.class.getResource("Image/gamePauseBtn.png")
//                                    .toString(),60,60,true,false));
//
//
//
//
//
//    //Class for handle rendering each frame
//    public static class GameAnimationTimer extends AnimationTimer{
//
//        private boolean isPause = false;
//        private long lastTime = System.nanoTime();
//
//        public void setupGame(){
//
//            //hide cursor
//            gameScene.setCursor(Cursor.NONE);
//
//            //add button
//            gamePauseBtnIcon.setPreserveRatio(true);
//            VBox newV = new VBox();
//            Button gamePauseBtn = new Button();
//            newV.getChildren().add(gamePauseBtn);
//            StackPane.setAlignment(gamePauseBtn, Pos.TOP_LEFT);
//            gamePauseBtn.setTranslateX(20);
//            gamePauseBtn.setTranslateY(20);
//            gamePauseBtn.setPrefSize(60,60);
//            gamePauseBtn.setMinHeight(gamePauseBtn.getPrefHeight());
//            gamePauseBtn.setMinWidth(gamePauseBtn.getPrefWidth());
//            gamePauseBtn.setOnMouseClicked(mouseEvent -> {
//                if(!isPause){
//                    stop();
//                }
//                else {
//                    start();
//                }
//            });
//
//
//            gamePauseBtn.setGraphic(gamePauseBtnIcon);
//            gameScene.getChildren().add(gamePauseBtn);
//
//
//            //detect if mouse stop moving
//            BooleanProperty mouseMoving = new SimpleBooleanProperty();
//            mouseMoving.addListener((obs, wasMoving, isNowMoving) -> {
//                if (! isNowMoving) {
//                    cursor.setMoving(false);
//                }
//            });
//            PauseTransition pause = new PauseTransition(Duration.millis(MIN_STATIONARY_TIME / 1_000_000));
//            pause.setOnFinished(e -> mouseMoving.set(false));
//
//            //=======<user input when in gameScene>===========================
//            scene.addEventHandler(MouseEvent.ANY , e->{
//                mouseMoving.set(true);
//                pause.playFromStart();
//            });
//            scene.setOnMouseMoved(event->{
//                cursor.setPosition(event.getSceneX() ,event.getSceneY());
//            });
//            scene.setOnMouseDragged(event->{
//                if(!isMouseClicked && !animationTimer.isPause()) {
//                    SoundManager.getInstance().playSoundEffect("Knife_Whoosh");
//                    isMouseClicked=true;
//                }
//                isMouseDragged=true;
//                cursor.setMoving(true);
//                cursor.setClicked(true);
//                cursor.setPosition(event.getSceneX() ,event.getSceneY());
//                cursor.makeTrailEffect();
//            });
//            scene.setOnMouseReleased(event->{
//                isMouseDragged=false;
//                isMouseClicked=false;
//                cursor.setClicked(false);
//            });
//            scene.setOnMouseDragExited(event->{
//                cursor.setMoving(false);
//            });
//            //pause game on ESC
//            scene.setOnKeyPressed(event -> {
//                KeyCode key = event.getCode();
//                if(key == KeyCode.ESCAPE){
//                    if(!isPause) {
//                        this.stop();
//                    }
//                    else {
//                        this.start();
//                    }
//                }
//            });
//            //==============================================
//        }
//
//        @Override
//        public void handle(long now) {
//
//            double elapsedTime = (now - lastTime)/1e9; //from millisec to sec
//            lastTime = now;
//
//            accumulateTime+=elapsedTime;
//
//            while(accumulateTime >= DeltaTime) { //will run with frame capped
//                gameUpdate();
//                gc.setFill(Color.web("BLACK"));
//                gc.fillText("FPS: " + Math.round(1.0 / elapsedTime) + "\ncursor.isMoving: " + cursor.isMoving() + "\nmouse(X,Y): " + cursor.getPosX() + "," + cursor.getPosY(), 20, 70);
//                accumulateTime-=elapsedTime;
//            }
//            gc.setFill(Color.web("BLACK"));
//            gc.fillText("FPS: " + Math.round(1.0 / elapsedTime),70,400);
//
//        }
//
//        @Override
//        public void start(){
//            super.start();
//            setupGame();
//            lastTime=System.nanoTime();
//            isPause=false;
//        }
//        @Override
//        public void stop(){
//            super.stop();
//            gameScene.setCursor(Cursor.DEFAULT);
//            isPause=true;
//            gc.setFill(Color.rgb(0,0,0,0.6));
//            gc.fillRect(0,0,WIDTH,HEIGHT);
//        }
//
//        public boolean isPause() {
//            return isPause;
//        }
//
//        //drawing game on screen
//        public void gameUpdate(){
//            //refresh screen every frame
//            gc.clearRect(0, 0, WIDTH, HEIGHT);
//            gc.drawImage(Main.gamebackground,0,0);
//            //spawn fruit every 1.5 sec
//            spawnTimer += DeltaTime;
//            if (spawnTimer > 2) {
//                //random number to generate
//                Random rand = new Random();
//                int totalGenerated = rand.nextInt(2, 5);
//                for (int i = 0; i < totalGenerated; i++) {
//                    tm.createFruits();
//                }
//                spawnTimer = 0;
//            }
//            ThrowerManager.getInstance().drawFruit(tm.getFruits() , cursor);
//
//            //draw cursor and its particles
//            ParticleManager.getInstance().particleUpdate();
//            cursor.drawCursor();
//
//            //debug text
//            gc.setFont(new Font("Segoe UI Black", 32));
//            gc.setFill(Color.web("WHITE"));
//
//        }
//
//        public void pauseCount(){
//
//        }
//    }
//
//    public static class CursorAnimationTimer extends AnimationTimer{
//
//
//        @Override
//        public void handle(long now){
//
//        }
//    }
//
//
//    //-----<START GAME WINDOW>--------------------------------------------------------
//    @Override
//    public void start(Stage primaryStage) throws IOException {
//
//        nodes.add(gameScene);
//        nodes.add(mainMenu);
//
//        //add in-game button
//
//
//
//        //add canvas into gamePane
//        gameScene.getChildren().addAll(canvas);
//        //root.setBackground(new Background(backgroundImage));
//
//        System.out.println(Main.class.getResource("Image/gameBackground1.png"));
//
//        //put 'scene' on stage and start render
//        primaryStage.setResizable(false);
//        primaryStage.setTitle("VegNinja");
//        primaryStage.setScene(scene);
//        primaryStage.show();
//
//
//    }
//    public static void main(String[] args){
//        launch(args);
//    }
//
//    //-----<Method>--------------------------------------------------------
//
//    public static void changeScene(int index){
//        scene.setRoot(nodes.get(index));
//    }
//
//    public Main() throws IOException {
//    }
//
//    public static void gamePause(){
//        animationTimer.stop();
//    };
//
//    public static void gameStart(){
//        if(animationTimer==null){
//            animationTimer=new GameAnimationTimer();
//        }
//        animationTimer.start();
//    };
//}



//THIS IS TEST SECTION
import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class Main extends Application{
    private static final double MIN_STATIONARY_TIME = 100_000_000;
    //Screen size
    public static int HEIGHT = Config.screenHEIGHT;
    public static int WIDTH = Config.screenWIDTH;

    public static double spawnTimer = 0; //timer for fruit spawning
    public static boolean isMouseDragged = false;
    public static boolean isMouseClicked = false;
    public static boolean isRunning = true; //game state

    //value for setting up game rendering (some magic to prevent inconsistent framerate)

    //create node to add into scene
    public static StackPane gameScene = new StackPane(); //Our main game
    public static Parent mainMenu;//main menu when the game booted
    static {
        try {
            mainMenu = FXMLLoader.load(Main.class.getResource("MainMenu.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //arrayList of node for Scene switching
    public static ArrayList<Parent> nodes = new ArrayList<>();
    //managing fruit
    public static ThrowerManager tm = new ThrowerManager();
    //cursor instance
    public static GameCursor cursor = new GameCursor();
    public static ParticleManager particleManager = ParticleManager.getInstance();
    //Media instance
    SoundManager soundManager = SoundManager.getInstance();
    public static ArrayList<Scene> Scenes = new ArrayList<>();

    //init scene for adding another layer on it
    public static Scene scene = new Scene(mainMenu, WIDTH , HEIGHT);
    public static Canvas canvas = new Canvas(WIDTH, HEIGHT); //canvas for drawing
    public static GraphicsContext gc = canvas.getGraphicsContext2D(); //we'll draw things using this

    public static GameAnimationTimer animationTimer;

    //load image
    public static Image img1 = new Image(Main.class.getResource("Image/gameBackground1.png")
            .toString(),WIDTH,HEIGHT,true,false);

    public static ImageView img2 = new ImageView(
            new Image(Main.class.getResource("Image/gamePauseBtn.png")
                    .toString(),60,60,true,false));

    //public static Image img3 = new Image(Main.class.getResource("Image/Murasama.png").toString(),100,120,true,false);
    //public static ImageView cursorIcon = new ImageView(img3);

    public static Image img4 = new Image(Main.class.getResource("Image/murasamaSprite.png").toString(),2465,260,true,false);
    public static Image img7 = new Image(Main.class.getResource("Image/murasamaEffect_Left.png").toString(),2465,260,true,false);

    public static ImageView murasamaSpriteRight = new ImageView();
    public static ImageView murasamaSpriteLeft = new ImageView();
    public static spriteAnimation cursorSprite = new spriteAnimation(murasamaSpriteRight,img4,13,0,13,(int)img4.getWidth()/13,(int)img4.getHeight(),16f);
    public static spriteAnimation cursorSpriteLeft = new spriteAnimation(murasamaSpriteLeft,img7,13,0,13,(int)img4.getWidth()/13,(int)img4.getHeight(),16f);


    public static Timer timer;

    //Class for handle rendering each frame
    public static class GameAnimationTimer extends AnimationTimer{

        private boolean isPause = true;
        private long lastTime = System.nanoTime();
        private long countdown = Config.GameTimer;
        private Button gamePauseBtn = new Button();
        public static double deltaTime;

        public void setupGame(){

            //add pause button
            StackPane.setAlignment(gamePauseBtn, Pos.TOP_LEFT);
            gamePauseBtn.setTranslateX(20); gamePauseBtn.setTranslateY(20); gamePauseBtn.setPrefSize(60,60);
            gamePauseBtn.setMinHeight(gamePauseBtn.getPrefHeight());    gamePauseBtn.setMinWidth(gamePauseBtn.getPrefWidth());
            gamePauseBtn.setOnMouseClicked(mouseEvent -> {
                System.out.println("CLICK");
                if(!animationTimer.isPause()){
                    animationTimer.stop();
                }
                else animationTimer.start();

            });
            gamePauseBtn.setGraphic(img2);

            //hide cursor
            gameScene.setCursor(Cursor.NONE);
            //detect if mouse stop moving
            BooleanProperty mouseMoving = new SimpleBooleanProperty();
            mouseMoving.addListener((obs, wasMoving, isNowMoving) -> {
                if (!isNowMoving) {
                    cursor.setMoving(false);
                }
            });
            PauseTransition pause = new PauseTransition(Duration.millis(MIN_STATIONARY_TIME / 1_000_000));
            pause.setOnFinished(e -> {
                mouseMoving.set(false);
            });

            //=======<user input when in gameScene>===========================
            scene.addEventHandler(MouseEvent.ANY , e->{
                mouseMoving.set(true);
                pause.playFromStart();
            });
            scene.setOnMouseClicked(event-> {
                cursor.setClicked(true);
            });
            scene.setOnMouseMoved(event->{
                //sword and effect will follow mouse
                cursor.setPosition(event.getSceneX() ,event.getSceneY());
                murasamaSpriteRight.setTranslateX(event.getSceneX()- murasamaSpriteRight.getX()-(murasamaSpriteRight.getImage().getWidth()/26) + 30);
                murasamaSpriteRight.setTranslateY(event.getSceneY()- murasamaSpriteRight.getY()-(murasamaSpriteRight.getImage().getHeight()/2) - 20);

            });
            scene.setOnMouseDragged(event->{
                if(!isMouseClicked && !animationTimer.isPause())
                    isMouseClicked=true;

                isMouseDragged=true;
                cursor.setMoving(true);
                cursor.setClicked(true);

                //make custom cursor follow your mouse
                cursor.setPosition(event.getSceneX(), event.getSceneY());

                //these things won't occur when game PAUSED
                if(!isPause) {
                    //sword and effect follow your mouse
                    murasamaSpriteRight.setTranslateX(event.getSceneX() - murasamaSpriteRight.getX() - (murasamaSpriteRight.getImage().getWidth() / 26) + 30);
                    murasamaSpriteRight.setTranslateY(event.getSceneY() - murasamaSpriteRight.getY() - (murasamaSpriteRight.getImage().getHeight() / 2) - 20);
                    cursor.makeTrailEffect();
                    cursorSprite.start();
                }

            });
            scene.setOnMouseReleased(event->{
                isMouseDragged=false;
                isMouseClicked=false;
                cursor.setClicked(false);
            });
            //pause game on ESC
            scene.setOnKeyPressed(event -> {
                KeyCode key = event.getCode();
                if(key == KeyCode.ESCAPE){
                    if(!this.isPause) {
                        this.stop();
                    }
                    else {
                        this.start();
                    }
                }
            });
            //in case cursor on Pause Button , the cursor will still follow over the button.
            gamePauseBtn.setOnMouseMoved(event -> cursor.setPosition(event.getSceneX(), event.getSceneY()));
            //==============================================


        }

        @Override
        public void handle(long now) {

            deltaTime = (now - lastTime)/1e9; //from millisec to sec
            lastTime = now;

            gameUpdate();


            gc.setFill(Color.web("BLACK"));
            gc.setFont(Font.font("Segoe UI BLACK",32));
            gc.fillText("FPS: " + Math.round(1.0 / deltaTime),70,400);
            gc.setLineWidth(5);
            gc.strokeText(Math.floorDiv(countdown,60) + " : " + (countdown%60==0? "00" : countdown%60) , WIDTH-200 , 50);
            gc.setFill(Color.web("WHITE"));
            gc.fillText( Math.floorDiv(countdown,60) + " : " + (countdown%60==0? "00" : countdown%60) , WIDTH-200 , 50);

        }
        @Override
        public void start(){
            if(isPause){
                this.isPause=false;
                super.start();
                setupGame();
                lastTime=System.nanoTime();
                startTimer();
            }

        }
        @Override
        public void stop(){
            if(!isPause) {
                this.isPause = true;
                super.stop();
                pauseTimer();
                //blacked screen when pause
                gc.setFill(Color.rgb(0, 0, 0, 0.6));
                gc.fillRect(0, 0, WIDTH, HEIGHT);
            }
        }

        public boolean isPause() {
            return isPause;
        }

        public void startTimer(){
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if(countdown>0){
                        System.out.println(countdown);
                        countdown--;
                    }
                }
            },1500 , 1000);
        }

        public void pauseTimer(){;
            timer.cancel();
        }

        //drawing game on screen
        public void gameUpdate(){
            //refresh screen every frame
            gc.clearRect(0, 0, WIDTH, HEIGHT);
            gc.drawImage(Main.img1,0,0);
            //spawn fruit every 1.5 sec
            spawnTimer += GameAnimationTimer.deltaTime;
            if (spawnTimer > 2) {
                //random number to generate
                Random rand = new Random();
                int totalGenerated = rand.nextInt(2, 5);
                for (int i = 0; i < totalGenerated; i++) {
                    tm.createFruits();
                }
                spawnTimer = 0;
            }
            ThrowerManager.getInstance().drawFruit(tm.getFruits() , cursor);

            //draw cursor and its particles
            cursor.drawCursor();


        }
    }



    //-----<START GAME WINDOW>--------------------------------------------------------
    @Override
    public void start(Stage primaryStage) throws IOException {

        //stop timer after force close
        primaryStage.setOnCloseRequest(windowEvent -> timer.cancel());


        nodes.add(gameScene);
        nodes.add(mainMenu);
        //StackPane.setAlignment(cursorIcon, Pos.TOP_LEFT);
        StackPane.setAlignment(murasamaSpriteRight,Pos.TOP_LEFT);
        StackPane.setAlignment(cursor.getCursorCircle(),Pos.TOP_LEFT);
        //cursorIcon.setPreserveRatio(true);
        //cursorIcon.setVisible(false);
        murasamaSpriteRight.setVisible(false);
        //cursorIcon.setRotate(cursorIcon.getRotate()+50);


        //add button
        img2.setPreserveRatio(true);

        animationTimer = new GameAnimationTimer();

        gameScene.getChildren().addAll(canvas, murasamaSpriteRight, animationTimer.gamePauseBtn, cursor.getCursorCircle());


        //put 'scene' on stage and start render
        primaryStage.setResizable(false);
        primaryStage.setTitle("VegNinja");
        primaryStage.setScene(scene);
        primaryStage.show();

    }
    public static void main(String[] args){

        launch(args);
    }

    //-----<Method>--------------------------------------------------------

    public static void changeScene(int index){
        scene.setRoot(nodes.get(index));
    }

    public Main() throws IOException {
    }

    public static void gamePause(){
        animationTimer.stop();
    };

    public static void gameStart(){
        if(animationTimer==null){
            animationTimer=new GameAnimationTimer();
        }
        animationTimer.start();
    };
}
