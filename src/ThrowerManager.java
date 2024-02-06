import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;

public class ThrowerManager {

    private ArrayList<Fruit> fruits;
    private static ThrowerManager instance;

    public static ThrowerManager getInstance(){
        if(instance == null){
            instance = new ThrowerManager();
        }
        return instance;
    }

    public ThrowerManager(){
        fruits = new ArrayList<>();
    }

    public ArrayList<Fruit> getFruits(){
        return fruits;
    }

    public void createFruits(){
        double X , Y , velx = -100, vely=0;
        int width , height , colorIndex;
        //random fruit parameter
        Random a = new Random();
        X = a.nextDouble(1);
        Y = a.nextDouble(20,40);
        width = a.nextInt(60,100);
        height = a.nextInt(60,80);
        vely = a.nextDouble(0,350)-1200;
        colorIndex = a.nextInt(6);//random 0-5

        //set velx according to spawn location
        if(X*Main.WIDTH < Main.WIDTH /3.0){//left
            velx = a.nextDouble(100,300);
        } else if (Main.WIDTH /3.0 <= X*Main.WIDTH && X*Main.WIDTH <= Main.WIDTH*(2.0/3.0)) {//middle
            velx = a.nextDouble(0,200)-100;
        }else if(X*Main.WIDTH > Main.WIDTH*(2.0/3.0)){//right
            velx = a.nextDouble(0,200)-300;
        }

        //add fruit to fruitList
        fruits.add(new Fruit(velx,vely,"test",Color.web(Fruit.getColorList()[colorIndex]),X*Main.WIDTH,Main.HEIGHT+Y , width , height));
    }

    public void drawFruit(ArrayList<Fruit> fruits , GameCursor cursor){

        for(int i = 0 ; i < fruits.size() ; i++) {
            Fruit f = fruits.get(i);
            //will remove fruit which exists for too long
            if (f.checkTimeout()) {
                fruits.remove(i);
                i--;
                continue;
            }

            if (cursor.isClicked() && cursor.isMoving() && cursor.isInFruit(f)) {//break fruit when hit by cursor
                SoundManager.getInstance().playSoundEffect("Fruit_Slash");
                Main.gc.setFill(Color.web("RED"));
                Main.gc.fillRect(f.getPosX(), f.getPosY(), f.getWidth(), f.getHeight());
                fruits.remove(i);
                i--;
            } else {
                Main.gc.setFill(fruits.get(i).getColor());
                Main.gc.fillRect(f.getPosX(), f.getPosY(), f.getWidth(), f.getHeight());
                f.setPosX(fruits.get(i).getPosX() + fruits.get(i).getVelX() * Main.GameAnimationTimer.deltaTime);
                f.setPosY(fruits.get(i).getPosY() + fruits.get(i).getVelY() * Main.GameAnimationTimer.deltaTime);
                f.update();
            }
        }
    }

}
