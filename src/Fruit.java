import javafx.scene.paint.Color;

public class Fruit {
    private double velX;
    private double velY;
    private String name;
    private Color color;
    private double posX;
    private double posY;
    private int width;
    private int height;
    public static final double gravity = 20;
    private double despawnTimer=0;
    private boolean isTimeout = false;
    private static final String[] colorList = {"CADETBLUE" , "CORAL" , "DARKORANGE" , "DEEPPINK" , "GOLD" , "GREENYELLOW"};

    //-----<constructor>---------------------------------------------------
    public Fruit(double velX, double velY, String name, Color color, double posX, double posY , int width , int height) {
        this.velX = velX;
        this.velY = velY;
        this.name = name;
        this.color = color;
        this.posX = posX;
        this.posY = posY;
        this.width=width;
        this.height=height;
    }

    //-----<getters&setters>-----------------------------------------------

    public double getVelX() {
        return velX;
    }

    public void setVelX(double velX) {
        this.velX = velX;
    }

    public double getVelY() {
        return velY;
    }

    public void setVelY(double velY) {
        this.velY = velY;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        name = name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public double getDespawnTimer() {
        return despawnTimer;
    }

    public void setDespawnTimer(double despawnTimer) {
        this.despawnTimer = despawnTimer;
    }

    public boolean isTimeout() {
        return isTimeout;
    }

    public void setTimeout(boolean timeout) {
        isTimeout = timeout;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public static String[] getColorList() {
        return colorList;
    }

    //-----<Method>--------------------------------------------------------

    public void physicHandle(){
        velY+=gravity;
        if(velX!=0) velX-= 1 * (Math.abs(velX)/velX);
    }

    public void update(){
        physicHandle();
        despawnTimer+=Main.DeltaTime;
        if(despawnTimer>=10) isTimeout = true;
    }

    public boolean checkTimeout(){
        return isTimeout;
    }
}
