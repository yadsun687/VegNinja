import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.Glow;
import javafx.scene.paint.Color;

import java.awt.*;


public class GameCursor {
    private double posX;
    private double posY;
    private double prevPosX;
    private double prevPosY;
    private final double radius = 5;
    private boolean isClicked;


    //-----<getters&setters>-----------------------------------------------
    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.prevPosX=this.posX;
        this.posX = posX;
    }

    public double getPrevPosX() {
        return prevPosX;
    }

    public void setPrevPosX(double prevPosX) {
        this.prevPosX = prevPosX;
    }

    public double getPrevPosY() {
        return prevPosY;
    }

    public void setPrevPosY(double prevPosY) {
        this.prevPosY = prevPosY;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.prevPosY=this.posY;
        this.posY = posY;
    }

    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }


    //-----<constructor>---------------------------------------------------
    public GameCursor() {
        this.posX = Main.WIDTH/2.0;
        this.posY = Main.HEIGHT/2.0;
    }

    //-----<Method>--------------------------------------------------------

    public void drawCursor(){
        if(!isClicked){
            Main.gc.setStroke(Color.web("WHITE"));
        }
        else Main.gc.setStroke(Color.web("RED"));

        Main.gc.setLineWidth(3);
        Main.gc.strokeOval((posX)-radius, (posY)-radius, radius * 2, radius * 2);

    }

    //create cursor's trails
    public void makeTrailEffect(){
        double[] unitVector = calculateDirection();
        //ParticleManager.getInstance().createCursorParticle(prevPosX , prevPosY , -(unitVector[0]*50) , -(unitVector[1]*50));
        ParticleManager.getInstance().createCursorParticle(prevPosX , prevPosY , 0 , 0);
    }

    //return 2DVector of cursor direction
    public double[] calculateDirection(){
        double[] vector = {posX - prevPosX, posY - prevPosY};
        double denom = Math.sqrt(vector[0]*vector[0] + vector[1]*vector[1]);
        vector[0]/=denom;
        vector[1]/=denom;
        return vector;
    }

    //check if cursor is in Fruit object
    public boolean isInFruit(Fruit f){
        return ((f.getPosX() <= posX && posX <= f.getPosX() + f.getWidth()) && (f.getPosY() <= posY && posY <= f.getPosY() + f.getHeight()));
    }
}
