import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;

public class ThrowerManager {

    private ArrayList<Fruit> fruits;

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
}
