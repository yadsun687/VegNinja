import javafx.event.Event;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.Glow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.awt.*;


public class GameCursor {
    private double posX;
    private double posY;
    private double prevPosX;
    private double prevPosY;
    private final double radius = 8;
    private boolean isClicked = false;
    private boolean isMoving = false;
    private Circle cursorCircle;


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

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    public Circle getCursorCircle() {
        return cursorCircle;
    }

    //-----<constructor>---------------------------------------------------
    public GameCursor() {

        //place outside window before game start
        this.posX = -80;
        this.posY = -80;

        //init cursor's circle
        cursorCircle = new Circle();
        cursorCircle.setRadius(radius);
        cursorCircle.setFill(Color.web("RED"));
        cursorCircle.setMouseTransparent(true);
        cursorCircle.setStrokeWidth(2);
    }

    //-----<Method>--------------------------------------------------------

    public void setPosition(double posX , double posY){

        cursorCircle.setTranslateX(posX - cursorCircle.getCenterX() - radius);
        cursorCircle.setTranslateY(posY - cursorCircle.getCenterY() - radius);

        setPosX(posX);
        setPosY(posY);
    }

    //draw cursor
    public void drawCursor(){
        if(!isClicked){
            cursorCircle.setStroke(Color.web("RED"));
        }
        else {
            cursorCircle.setStroke(Color.web("WHITE"));
        }

        ParticleManager.getInstance().particleUpdateAndDraw();

    }

    //create cursor's trails
    public void makeTrailEffect(){
        ParticleManager.getInstance().createCursorParticle(posX , posY , 0 , 0);
    }

    //check if cursor is in Fruit object
    public boolean isInFruit(Fruit f){

        //HITBOX is 1.5 time the cursor radius
        double CursorTopLeftX = posX-(radius*1.5);
        double CursorTopLeftY = posY-(radius*1.5);
        double CursorBotRightX = posX+(radius*1.5);
        double CursorBotRightY = posY+(radius*1.5);

        double FruitTopLeftX = f.getPosX();
        double FruitTopLeftY = f.getPosY();
        double FruitBotRightX = f.getPosX()+f.getWidth();
        double FruitBotRightY = f.getPosY()+f.getHeight();

        //true if HITBOX of cursor not overlap fruit
        return !( (CursorTopLeftX > FruitBotRightX || CursorBotRightX < FruitTopLeftX) ||
                (CursorTopLeftY > FruitBotRightY || CursorBotRightY < FruitTopLeftY) );

    }
}
