//import javafx.animation.AnimationTimer;
//import javafx.scene.paint.Color;
//import javafx.scene.text.Font;
//
//import java.util.Random;
//
//public class GameAnimationTimer extends AnimationTimer {
//
//    private final double frameRate;
//    private final double DeltaTime;
//    private long lastTime = System.nanoTime();
//    private double accumulateTime =0.0;
//    private final int WIDTH;
//    private final int HEIGHT;
//
//    public GameAnimationTimer(int width , int height ,double fps){
//        this.WIDTH=width;
//        this.HEIGHT=height;
//        this.frameRate=fps;
//        DeltaTime=1.0/frameRate;
//    }
//
//    @Override
//    public void handle(long now) {
//        double elapsedTime = (now-lastTime)/1e9; //from millisec to sec
//        lastTime = now;
//
//        accumulateTime+=elapsedTime;
//        while(accumulateTime >= DeltaTime) { //will run with frame capped
//
//            //refresh screen every frame
//            gc.clearRect(0, 0, WIDTH, HEIGHT);
//
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
//            gc.fillText("FPS: " + (1.0 / elapsedTime) + "\ncursor.isMoving: " + cursor.isMoving() + "\nmouse(X,Y): " + cursor.getPosX() + "," + cursor.getPosY(), 20, 70);
//
//            accumulateTime-= DeltaTime;
//        }
//    }
//}