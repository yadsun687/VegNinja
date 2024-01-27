public class CursorParticle {
    private double posX;
    private double posY;
    private double velX;
    private double velY;
    private double despawnTimer=0;
    private boolean isTimeout = false;

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

    public CursorParticle(double posX, double posY, double velX, double velY) {
        this.posX = posX;
        this.posY = posY;
        this.velX = velX;
        this.velY = velY;
    }

    public void update(){
        if(despawnTimer>0.1) isTimeout=true;
        posX+=velX*Main.DeltaTime;
        posY+=velY*Main.DeltaTime;
        despawnTimer+=Main.DeltaTime;
    }

}
